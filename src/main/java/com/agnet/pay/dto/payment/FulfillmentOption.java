package com.agnet.pay.dto.payment;

import lombok.Data;

@Data
public class FulfillmentOption {
    private String id;
    private String label;
    private long priceCents;

    public FulfillmentOption() {}
    public FulfillmentOption(String id, String label, long priceCents) {
        this.id = id; this.label = label; this.priceCents = priceCents;
    }

}
