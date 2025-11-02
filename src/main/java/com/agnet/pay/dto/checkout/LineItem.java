package com.agnet.pay.dto.checkout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LineItem {
    private String id;
    private Item item;
    @JsonProperty("base_amount")
    private int baseAmount;
    private int discount;
    private int subtotal;
    private int tax;
    private int total;

}
