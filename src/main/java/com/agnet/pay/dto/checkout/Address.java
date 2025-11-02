package com.agnet.pay.dto.checkout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    private String name;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("line_one")
    private String lineOne;
    @JsonProperty("line_two")
    private String lineTwo;
}
