package com.agnet.pay.dto.payment;

import com.agnet.pay.dto.checkout.Address;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DelegatePaymentRequest {
    private PaymentMethod paymentMethod;
    private Allowance allowance;
    private Address billingAddress;
    private List<RiskSignal> riskSignals;
    private Map<String, String> metadata;
}
