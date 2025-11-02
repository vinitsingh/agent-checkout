package com.agnet.pay.configurations;

import com.agnet.pay.dto.checkout.CheckoutRequest;
import com.agnet.pay.dto.checkout.CheckoutResponse;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    public static final Map<String, CheckoutResponse> SESSIONS = new ConcurrentHashMap<>();
    public static final Map<String, String> IDEMPOTENCY = new ConcurrentHashMap<>();
    public static final Map<String, Map<String,Object>> delegatedTokens = new HashMap<>();
    public static KeyPair AGENT_KEYPAIR;
    public static KeyPair PSP_KEYPAIR;

    static {
        try {
            java.security.KeyPairGenerator kpg = java.security.KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            AGENT_KEYPAIR = kpg.generateKeyPair();
            PSP_KEYPAIR = kpg.generateKeyPair();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
