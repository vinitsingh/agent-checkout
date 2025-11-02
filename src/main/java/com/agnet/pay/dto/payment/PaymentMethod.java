package com.agnet.pay.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentMethod {
    private String type; // "card"
    private String cardNumberType; // "network_token" or "fpan"
    private String cryptogram;
    private String eciValue;
    private String displayLast4;
    private String displayBrand;
    private Map<String,String> metadata;
}
