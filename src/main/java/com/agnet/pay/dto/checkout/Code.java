package com.agnet.pay.dto.checkout;

public enum Code {

    success,
    missing,
    invalid,
    out_of_stock,
    payment_declined,
    requires_sign_in,
    requires_3ds;
}
