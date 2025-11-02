package com.agnet.pay.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentMethod {
    private String type;
    private String cardNumberType;
    private String cryptogram;
    private String eciValue;
    private String displayLast4;
    private String displayBrand;
    private Map<String,String> metadata;
}
