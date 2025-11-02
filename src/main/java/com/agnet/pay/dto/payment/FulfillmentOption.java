package com.agnet.pay.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FulfillmentOption {
    private String type;
    private String id;
    private String title;
    private String subtitle;
    private int subtotal;
    private int tax;
    private int total;
    @JsonProperty("earliest_delivery_time")
    private  String earliestDeliveryTime;
    @JsonProperty("latest_delivery_time")
    private String latestDeliveryTime;
}
