package com.agnet.pay.api;


import com.agnet.pay.configurations.DataStore;
import com.agnet.pay.dto.payment.PaymentRequest;
import com.agnet.pay.dto.payment.PaymentResponse;
import com.agnet.pay.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/agentic_commerce")
@AllArgsConstructor
@Slf4j
public class PaymentController {

    private final TokenService tokenService;

    @PostMapping("/delegate_payment")
    public ResponseEntity<?> delegatePayment(@RequestBody PaymentRequest req,
                                             @RequestHeader(value = "Authorization", required = true) String authorization,
                                             @RequestHeader(value = "User-Agent", required = true) String userAgent,
                                             @RequestHeader(value = "Request-Id", required = true) String RequestId,
                                             @RequestHeader(value = "Signature", required = true) String signature,
                                             @RequestHeader(value = "Timestamp", required = true) String timestamp,
                                             @RequestHeader(value = "API-Version", required = true) String apiVersion,
                                             @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) throws Exception {
        // TODO: Add validation
        if (req.getPaymentMethod() == null || req.getAllowance() == null || req.getMetadata() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "invalid_request", "message", "Missing required fields"));
        }
        String id = "FI#" + Instant.now().toEpochMilli();
        String token = tokenService.issueDelegatedToken(
                id,
                req.getAllowance().getCheckoutSessionId(),
                req.getAllowance().getMaxAmount(),
                req.getAllowance().getCurrency(),
                req.getAllowance().getMerchantId(),
                600,
                req.getPaymentMethod().getCardNumberType(),
                req.getPaymentMethod().getNumber(),
                req.getPaymentMethod().getExpMonth(),
                req.getPaymentMethod().getExpYear());

        DataStore.delegatedTokens.put(id, Map.of("jwt", token, "metadata", req.getMetadata()));

        PaymentResponse resp = new PaymentResponse();
        resp.setId(id);
        resp.setCreated(Instant.now());
        resp.setMetadata(Map.of("checkout_session_id", req.getAllowance().getCheckoutSessionId(), "token_jwt", token));
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
