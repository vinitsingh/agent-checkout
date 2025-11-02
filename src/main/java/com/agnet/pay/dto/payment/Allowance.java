package com.agnet.pay.dto.payment;

import lombok.Data;

import java.time.Instant;

@Data
public class Allowance {
    private String reason;
    private long maxAmount;
    private String currency;
    private String checkoutSessionId;
    private String merchantId;
    private Instant expiresAt;
}
