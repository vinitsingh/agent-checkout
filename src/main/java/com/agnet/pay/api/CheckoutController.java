package com.agnet.pay.api;

import com.agnet.pay.configurations.DataStore;
import com.agnet.pay.dto.checkout.CheckoutRequest;
import com.agnet.pay.dto.checkout.CheckoutResponse;
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
    public ResponseEntity<?> create(@RequestBody CheckoutRequest checkoutRequest,
                                    @RequestHeader(value = "Authorization", required = true) String authorization,
                                    @RequestHeader(value = "User-Agent", required = true) String userAgent,
                                    @RequestHeader(value = "Request-Id", required = true) String RequestId,
                                    @RequestHeader(value = "Signature", required = true) String signature,
                                    @RequestHeader(value = "Timestamp", required = true) String timestamp,
                                    @RequestHeader(value = "API-Version", required = true) String apiVersion,
                                    @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {

        if (idempotencyKey != null && checkoutService.idempotencySeen(idempotencyKey)) {
            return ResponseEntity.ok(checkoutService.getSession(idempotencyKey));
        }
        CheckoutResponse response = checkoutService.createSession(checkoutRequest);
        if (idempotencyKey != null) checkoutService.recordIdempotency(idempotencyKey, response.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id,
                                    @RequestBody CheckoutRequest updates,
                                    @RequestHeader(value = "Authorization", required = true) String authorization,
                                    @RequestHeader(value = "User-Agent", required = true) String userAgent,
                                    @RequestHeader(value = "Request-Id", required = true) String RequestId,
                                    @RequestHeader(value = "Signature", required = true) String signature,
                                    @RequestHeader(value = "Timestamp", required = true) String timestamp,
                                    @RequestHeader(value = "API-Version", required = true) String apiVersion,
                                    @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        CheckoutResponse s = checkoutService.updateSession(id, updates);
        if (s == null) return ResponseEntity.status(404).body(Map.of("error", "session_not_found"));
        return ResponseEntity.ok(s);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id,
                                 @RequestHeader(value = "Authorization", required = true) String authorization,
                                 @RequestHeader(value = "User-Agent", required = true) String userAgent,
                                 @RequestHeader(value = "Request-Id", required = true) String RequestId,
                                 @RequestHeader(value = "Signature", required = true) String signature,
                                 @RequestHeader(value = "Timestamp", required = true) String timestamp,
                                 @RequestHeader(value = "API-Version", required = true) String apiVersion,
                                 @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        CheckoutResponse s = checkoutService.getSession(id);
        if (s == null) return ResponseEntity.status(404).body(Map.of("error", "session_not_found"));
        return ResponseEntity.ok(s);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable String id,
                                    @RequestHeader(value = "Authorization", required = true) String authorization,
                                    @RequestHeader(value = "User-Agent", required = true) String userAgent,
                                    @RequestHeader(value = "Request-Id", required = true) String RequestId,
                                    @RequestHeader(value = "Signature", required = true) String signature,
                                    @RequestHeader(value = "Timestamp", required = true) String timestamp,
                                    @RequestHeader(value = "API-Version", required = true) String apiVersion,
                                    @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        boolean ok = checkoutService.cancelSession(id);
        if (!ok) return ResponseEntity.status(404).body(Map.of("error", "session_not_found"));
        return ResponseEntity.ok(Map.of("status", "canceled", "id", id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable String id,
                                      @RequestBody CheckoutRequest body,
                                      @RequestHeader(value = "Authorization", required = true) String authorization,
                                      @RequestHeader(value = "User-Agent", required = true) String userAgent,
                                      @RequestHeader(value = "Request-Id", required = true) String RequestId,
                                      @RequestHeader(value = "Signature", required = true) String signature,
                                      @RequestHeader(value = "Timestamp", required = true) String timestamp,
                                      @RequestHeader(value = "API-Version", required = true) String apiVersion,
                                      @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {

        // Basic idempotency handling
        if (idempotencyKey != null && checkoutService.idempotencySeen(idempotencyKey)) {
            String existing = DataStore.IDEMPOTENCY.get(idempotencyKey);
            CheckoutResponse s = checkoutService.getSession(existing);
            return ResponseEntity.ok(s);
        }

        // Reconstruct canonical base string (PRODUCTION: implement RFC9421 canonicalization)
        String canonical = "\"@authority\": merchant.example\n\"@path\": /checkout_sessions/" + id + "/complete\n\"@signature-params\": " + (signature == null ? "(...)" : signature);

        // verify signature (simplified)
        boolean ok = sigService.verify(canonical, signature);
        if (!ok) return ResponseEntity.status(401).body(Map.of("error", "invalid_signature"));

        // Validate delegated token (merchant should call PSP or validate JWT)
        if (body.getPaymentData().getToken() == null)
            return ResponseEntity.status(400).body(Map.of("error", "missing_payment_token"));

        CheckoutResponse session = checkoutService.getSession(id);
        if (session == null) return ResponseEntity.status(404).body(Map.of("error", "session_not_found"));

        // TODO: This should call commerce hub Payment Auth API?
        //boolean verified = tokenService.validateDelegatedToken(body.getPaymentData().getToken(), "GIFT", total, id);
        if (true) {
            CheckoutResponse completed = checkoutService.completeSession(id);
            if (idempotencyKey != null) checkoutService.recordIdempotency(idempotencyKey, id);
            return ResponseEntity.ok(completed);
        }
        return ResponseEntity.badRequest().build();
    }
}