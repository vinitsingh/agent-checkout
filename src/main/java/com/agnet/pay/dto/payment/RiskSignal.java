package com.agnet.pay.dto.payment;

import lombok.Data;

@Data
public class RiskSignal {
    private String type;
    private int score;
    private String action;
}
