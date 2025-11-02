package com.agnet.pay.dto.payment;

import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class DelegatePaymentResponse {
    private String id;
    private Instant created;
    private Map<String,Object> metadata;
}
