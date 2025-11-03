package com.agnet.pay.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Allowance {
    private String reason;
    @JsonProperty("max_amount")
    private long maxAmount;
    private String currency;
    @JsonProperty("checkout_session_id")
    private String checkoutSessionId;
    @JsonProperty("merchant_id")
    private String merchantId;
    @JsonProperty("expires_at")
    private Instant expiresAt;
}
