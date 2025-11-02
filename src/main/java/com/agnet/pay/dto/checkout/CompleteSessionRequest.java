package com.agnet.pay.dto.checkout;

import lombok.Data;

@Data
public class CompleteSessionRequest {
    private String checkoutSessionId;
    private String paymentToken; // delegated token (one-time)
    private String intent;
    private Long totalCents;
}
