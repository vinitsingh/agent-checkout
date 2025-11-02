package com.agnet.pay.dto.checkout;

import lombok.Data;

@Data
public class Address {
    private String lineOne;
    private String lineTwo;
    private String city;
    private String state;
    private String country;
    private String postalCode;
}
