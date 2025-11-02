package com.agnet.pay.dto.checkout;

import com.agnet.pay.dto.payment.FulfillmentOption;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
public class CheckoutSession {
    private String id;
    private String status; // not_ready_for_payment, ready_for_payment, completed, canceled
    private String currency;
    private List<Map<String, Object>> lineItems;
    private Map<String, Object> buyer;
    private Address fulfillmentAddress;
    private List<FulfillmentOption> fulfillmentOptions;
    private Instant expiresAt;
    private Map<String, Object> metadata;
    private String delegatedToken; // optional
}
