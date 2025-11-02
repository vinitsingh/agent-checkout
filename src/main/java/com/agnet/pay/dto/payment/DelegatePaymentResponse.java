package com.agnet.pay.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DelegatePaymentResponse {
    private String id;
    private Instant created;
    private Map<String,Object> metadata;
}
