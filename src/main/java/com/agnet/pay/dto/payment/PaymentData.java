package com.agnet.pay.dto.payment;

import com.agnet.pay.dto.checkout.Address;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentData {
    private String token;
    private String provider;
    @JsonProperty("billing_address")
    private Address billingAddress;
}
