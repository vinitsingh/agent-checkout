package com.agnet.pay.dto.checkout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Buyer {

    @JsonProperty("first_name")
    private String firstName;
    private String email;
    @JsonProperty("phone_number")
    private String phoneNumber;

}
