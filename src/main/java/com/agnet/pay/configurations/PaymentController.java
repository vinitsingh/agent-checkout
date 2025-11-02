package com.agnet.pay.configurations;


import com.agnet.pay.data.DataStore;
import com.agnet.pay.dto.payment.DelegatePaymentRequest;
import com.agnet.pay.dto.payment.DelegatePaymentResponse;
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
    public ResponseEntity<?> delegatePayment(@RequestHeader(value = "Signature", required = false) String signature,
                                             @RequestBody DelegatePaymentRequest req,
                                             @RequestHeader(value = "Idempotency-Key", required = false) String idempotency) throws Exception {
        // TODO: Add validation
        if (req.getPaymentMethod() == null || req.getAllowance() == null || req.getMetadata() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "invalid_request", "message", "Missing required fields"));
        }

        // TODO: verify signature - PSP verify the agent signature
        // Issue delegated token (JWT) with scope
        String token = tokenService.issueDelegatedToken(
                req.getAllowance().getCheckoutSessionId(),
                req.getAllowance().getMaxAmount(),
                req.getAllowance().getCurrency(),
                req.getAllowance().getMerchantId(),
                600,
                Map.of("agent_id", req.getMetadata().get("agent_id"))
        );

        String id = "FI#" + Instant.now().toEpochMilli();

        DataStore.delegatedTokens.put(id, Map.of("jwt", token, "metadata", req.getMetadata()));

        DelegatePaymentResponse resp = new DelegatePaymentResponse();
        resp.setId(id);
        resp.setCreated(Instant.now());
        resp.setMetadata(Map.of("checkout_session_id", req.getAllowance().getCheckoutSessionId(), "token_jwt", token));
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
