package com.agnet.pay.dto.checkout;

import com.agnet.pay.dto.payment.FulfillmentOption;
import com.agnet.pay.dto.payment.PaymentData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckoutResponse {
    private String id;
    private Buyer buyer;
    @JsonProperty("payment_provider")
    private PaymentProvider paymentProvider;
    private Status status;
    private String currency;
    @JsonProperty("line_items")
    private List<LineItem> lineItems;
    @JsonProperty("fulfillment_address")
    private Address fulfillmentAddress;
    @JsonProperty("fulfillment_options")
    private List<FulfillmentOption> fulfillmentOptions;
    @JsonProperty("fulfillment_option_id")
    private String fulfillmentOptionId;
    private List<Total> totals;
    private List<Message> messages;
    private List<Link> links;
    @JsonProperty("payment_data")
    private PaymentData paymentData;
}
