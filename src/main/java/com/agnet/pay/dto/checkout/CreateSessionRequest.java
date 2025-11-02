package com.agnet.pay.dto.checkout;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreateSessionRequest {
    private Map<String,Object> buyer;
    private List<Map<String,Object>> items;
    private Address fulfillmentAddress;
    private Map<String,Object> metadata;
}
