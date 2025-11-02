package com.agnet.pay.configurations;

import com.agnet.pay.data.DataStore;
import com.agnet.pay.dto.checkout.CheckoutSession;
import com.agnet.pay.dto.checkout.CompleteSessionRequest;
import com.agnet.pay.dto.checkout.CreateSessionRequest;
import com.agnet.pay.service.CheckoutService;
import com.agnet.pay.service.SignatureVerificationService;
import com.agnet.pay.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/checkout_sessions")
@AllArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final SignatureVerificationService sigService;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateSessionRequest req,
                                    @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        if (idempotencyKey != null && checkoutService.idempotencySeen(idempotencyKey)) {
            String existing = DataStore.IDEMPOTENCY.get(idempotencyKey);
            CheckoutSession s = checkoutService.getSession(existing);
            return ResponseEntity.ok(Map.of("id", s.getId(), "status", s.getStatus()));
        }
        CheckoutSession s = checkoutService.createSession(req);
        if (idempotencyKey != null) checkoutService.recordIdempotency(idempotencyKey, s.getId());
        return ResponseEntity.ok(s);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        CheckoutSession s = checkoutService.updateSession(id, updates);
        if (s == null) return ResponseEntity.status(404).body(Map.of("error", "session_not_found"));
        return ResponseEntity.ok(s);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        CheckoutSession s = checkoutService.getSession(id);
        if (s == null) return ResponseEntity.status(404).body(Map.of("error", "session_not_found"));
        return ResponseEntity.ok(s);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable String id) {
        boolean ok = checkoutService.cancelSession(id);
        if (!ok) return ResponseEntity.status(404).body(Map.of("error", "session_not_found"));
        return ResponseEntity.ok(Map.of("status", "canceled", "id", id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable String id,
                                      @RequestHeader(value = "Signature-Input", required = false) String sigInput,
                                      @RequestHeader(value = "Signature", required = false) String signature,
                                      @RequestHeader(value = "Request-Id", required = false) String requestId,
                                      @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
                                      @RequestBody CompleteSessionRequest body) {

        // Basic idempotency handling
        if (idempotencyKey != null && checkoutService.idempotencySeen(idempotencyKey)) {
            String existing = DataStore.IDEMPOTENCY.get(idempotencyKey);
            CheckoutSession s = checkoutService.getSession(existing);
            return ResponseEntity.ok(s);
        }

        // Reconstruct canonical base string (PRODUCTION: implement RFC9421 canonicalization)
        String canonical = "\"@authority\": merchant.example\n\"@path\": /checkout_sessions/" + id + "/complete\n\"@signature-params\": " + (sigInput == null ? "(...)" : sigInput);

        // verify signature (simplified)
        boolean ok = sigService.verify(canonical, signature);
        if (!ok) return ResponseEntity.status(401).body(Map.of("error", "invalid_signature"));

        // Validate delegated token (merchant should call PSP or validate JWT)
        if (body.getPaymentToken() == null)
            return ResponseEntity.status(400).body(Map.of("error", "missing_payment_token"));

        // Optional: verify total matches session computed totals (demo uses provided total or line items)
        Long total = body.getTotalCents();

        CheckoutSession session = checkoutService.getSession(id);
        if (session == null) return ResponseEntity.status(404).body(Map.of("error", "session_not_found"));

        // compute total if not provided
        total = getTotal(total, session);

        // TODO: This should call commerce hub Payment Auth API
        boolean verified = tokenService.validateDelegatedToken(body.getPaymentToken(), "GIFT", total, id);
        if (verified) {
            CheckoutSession completed = checkoutService.completeSession(id, body.getPaymentToken(), total);
            if (idempotencyKey != null) checkoutService.recordIdempotency(idempotencyKey, id);
            return ResponseEntity.ok(completed);
        }
        return ResponseEntity.badRequest().build();
    }

    private static Long getTotal(Long total, CheckoutSession session) {
        if (total == null) {
            long sum = 0;
            if (session.getLineItems() != null) {
                for (var it : session.getLineItems()) {
                    Object p = it.getOrDefault("price", it.get("price_cents"));
                    if (p instanceof Number) sum += ((Number) p).longValue();
                }
            }
            total = sum;
        }
        return total;
    }
}