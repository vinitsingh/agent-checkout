package com.agnet.pay.dto.payment;

import com.agnet.pay.dto.checkout.Code;
import com.agnet.pay.dto.checkout.Message;

public class PaymentError {

    private PaymentError type;
    private Code code;
    private String message;
}
