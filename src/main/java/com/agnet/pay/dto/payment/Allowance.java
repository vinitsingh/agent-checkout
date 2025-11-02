package com.agnet.pay.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Allowance {
    private String reason;
    private long maxAmount;
    private String currency;
    private String checkoutSessionId;
    private String merchantId;
    private Instant expiresAt;
}
