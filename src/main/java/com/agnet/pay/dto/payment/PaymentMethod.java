package com.agnet.pay.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentMethod {
    private String type;
    @JsonProperty("card_number_type")
    private CardNumberType cardNumberType;
    private String number;
    @JsonProperty("exp_month")
    private String expMonth;
    @JsonProperty("exp_year")
    private String expYear;
    private String name;
    private String cvc;
    private String cryptogram;
    @JsonProperty("eci_value")
    private String eciValue;
    @JsonProperty("checks_performed")
    private String checksPerformed;
    private String iin;
    @JsonProperty("display_card_funding_type")
    private String displayCardFundingType;
    @JsonProperty("display_wallet_type")
    private String displayWalletType;
    @JsonProperty("display_brand")
    private String displayBrand;
    @JsonProperty("display_last4")
    private String displayLast4;
    private Map<String, String> metadata;
}
