package com.agnet.pay.dto.checkout;

import com.agnet.pay.dto.payment.PaymentData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckoutRequest {
    private Buyer buyer;
    private List<Item> items;
    @JsonProperty("fulfillment_address")
    private Address fulfillmentAddress;
    @JsonProperty("fulfillment_option_id")
    private String fulfillmentOptionId;
    @JsonProperty("payment_data")
    private PaymentData paymentData;
}
