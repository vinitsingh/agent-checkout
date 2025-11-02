package com.agnet.pay.dto.checkout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    private String id;
    @JsonProperty("checkout_session_id")
    private String checkout_session_id;
    @JsonProperty("permalink_url")
    private String permalink_url;

}
