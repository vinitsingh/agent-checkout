package com.agnet.pay.dto.payment;

import com.agnet.pay.dto.checkout.Address;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRequest {
    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;
    private Allowance allowance;
    @JsonProperty("billing_address")
    private Address billingAddress;
    @JsonProperty("risk_signals")
    private List<RiskSignal> riskSignals;
    private Map<String, String> metadata;
}
