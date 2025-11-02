package com.agnet.pay.dto.checkout;

import lombok.Data;

@Data
public class CompleteSessionRequest {
    private String checkoutSessionId;
    private String paymentToken;
    private String intent;
    private Long totalCents;
}
