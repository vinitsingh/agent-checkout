package com.agnet.pay.service;


import com.agnet.pay.data.DataStore;
import com.agnet.pay.dto.checkout.Address;
import com.agnet.pay.dto.checkout.CheckoutSession;
import com.agnet.pay.dto.checkout.CreateSessionRequest;
import com.agnet.pay.dto.payment.FulfillmentOption;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CheckoutService {

    public CheckoutSession createSession(CreateSessionRequest req) {
        String id = "sess_" + UUID.randomUUID().toString().substring(0, 8);
        CheckoutSession s = new CheckoutSession();
        s.setId(id);
        s.setStatus("not_ready_for_payment");
        s.setCurrency("USD");
        s.setLineItems(req.getItems());
        s.setBuyer(req.getBuyer());
        s.setFulfillmentAddress(req.getFulfillmentAddress());
        s.setFulfillmentOptions(List.of(new FulfillmentOption("ship_standard", "Standard Shipping", 500)));
        s.setExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS));
        s.setMetadata(req.getMetadata());
        DataStore.SESSIONS.put(id, s);
        return s;
    }

    public CheckoutSession updateSession(String id, Map<String, Object> updates) {
        CheckoutSession s = DataStore.SESSIONS.get(id);
        if (s == null) return null;
        if (updates.containsKey("line_items")) s.setLineItems((List<Map<String, Object>>) updates.get("line_items"));
        if (updates.containsKey("buyer")) s.setBuyer((Map<String, Object>) updates.get("buyer"));
        if (updates.containsKey("fulfillmentAddress"))
            s.setFulfillmentAddress((Address) updates.get("fulfillmentAddress"));
        if (updates.containsKey("metadata")) s.setMetadata((Map<String, Object>) updates.get("metadata"));
        return s;
    }

    public CheckoutSession getSession(String id) {
        return DataStore.SESSIONS.get(id);
    }

    public boolean cancelSession(String id) {
        CheckoutSession s = DataStore.SESSIONS.get(id);
        if (s == null) return false;
        s.setStatus("canceled");
        DataStore.SESSIONS.put(id, s);
        return true;
    }

    public CheckoutSession completeSession(String id, String delegatedToken, Long totalCents) {
        CheckoutSession s = DataStore.SESSIONS.get(id);
        if (s == null) return null;
        s.setDelegatedToken(delegatedToken);
        s.setStatus("completed");
        DataStore.SESSIONS.put(id, s);
        return s;
    }

    public boolean idempotencySeen(String idempotencyKey) {
        return DataStore.IDEMPOTENCY.containsKey(idempotencyKey);
    }

    public void recordIdempotency(String idempotencyKey, String sessionId) {
        DataStore.IDEMPOTENCY.put(idempotencyKey, sessionId);
    }
}
