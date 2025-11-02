package com.agnet.pay.dto.payment;

import com.agnet.pay.dto.checkout.Address;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DelegatePaymentRequest {
    private PaymentMethod paymentMethod;
    private Allowance allowance;
    private Address billingAddress;
    private List<RiskSignal> riskSignals;
    private Map<String, String> metadata;
}
