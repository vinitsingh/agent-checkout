package com.agnet.pay.configurations;


import com.agnet.pay.data.DataStore;
import com.agnet.pay.dto.payment.DelegatePaymentRequest;
import com.agnet.pay.dto.payment.DelegatePaymentResponse;
import com.agnet.pay.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/agentic_commerce")
public class PaymentController {

    @PostMapping("/delegate_payment")
    public ResponseEntity<?> delegatePayment(@RequestHeader(value = "Signature", required = false) String signature,
                                             @RequestBody DelegatePaymentRequest req,
                                             @RequestHeader(value = "Idempotency-Key", required = false) String idempotency) throws Exception {
        // Basic validation for demo
        if (req.getPaymentMethod() == null || req.getAllowance() == null || req.getMetadata() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "invalid_request", "message", "Missing required fields"));
        }

        // TODO: (Optional) verify signature - in a real PSP you'd verify the agent signature on body
        // TODO: For demo we skip parsing signature content but you can add verification here if agent public key known.

        // Issue delegated token (JWT) with scope
        String token = TokenService.issueDelegatedToken(
                req.getAllowance().getCheckoutSessionId(),
                req.getAllowance().getMaxAmount(),
                req.getAllowance().getCurrency(),
                req.getAllowance().getMerchantId(),
                600,
                Map.of("agent_id", req.getMetadata().get("agent_id"))
        );

        String id = "pay_tok_" + Instant.now().toEpochMilli();

        DataStore.delegatedTokens.put(id, Map.of("jwt", token, "metadata", req.getMetadata()));

        DelegatePaymentResponse resp = new DelegatePaymentResponse();
        resp.setId(id);
        resp.setCreated(Instant.now());
        resp.setMetadata(Map.of("checkout_session_id", req.getAllowance().getCheckoutSessionId(), "token_jwt", token));
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
