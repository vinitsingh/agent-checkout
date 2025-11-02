package com.agnet.pay.dto.checkout;

public enum Code {

    success,
    missing,
    invalid,
    out_of_stock,
    invalid_card,
    payment_declined,
    requires_sign_in,
    requires_3ds;
}
