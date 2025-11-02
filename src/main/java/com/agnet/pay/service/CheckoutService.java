package com.agnet.pay.service;


import com.agnet.pay.configurations.DataStore;
import com.agnet.pay.dto.checkout.*;
import com.agnet.pay.dto.payment.FulfillmentOption;
import com.agnet.pay.dto.payment.PaymentData;
import com.agnet.pay.dto.payment.PaymentMethod;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CheckoutService {

    public CheckoutResponse createSession(CheckoutRequest req) {
        CheckoutResponse response = new CheckoutResponse();
        String id = UUID.randomUUID().toString();
        response.setId(id);
        PaymentProvider paymentProvider = new PaymentProvider();
        paymentProvider.setProvider("Fiserv");
        paymentProvider.setSupportedPaymentMethods(List.of(SupportedPaymentMethods.card.name()));
        response.setPaymentProvider(paymentProvider);
        response.setStatus(Status.not_ready_for_payment);
        response.setCurrency("USD");

        // TODO: merchant Inventory lookup for Item
        Item item = new Item();
        LineItem lineItem = new LineItem();
        lineItem.setItem(item);

        // TODO: total calculation based on Item price and Tax
        Total total = new Total();
        response.setTotals(List.of(total));

        //TODO: based on merchant Inventory
        FulfillmentOption fulfillmentOption = new FulfillmentOption();
        response.setFulfillmentOptions(List.of(fulfillmentOption));

        //TODO:  business logic
        Message message = new Message();
        message.setCode(Code.success);
        response.setMessages(List.of(message));

        //TODO: business logic
        Link link = new Link();
        response.setLinks(List.of(link));

        DataStore.SESSIONS.put(id, response);
        return response;
    }

    public CheckoutResponse updateSession(String id, CheckoutRequest updates) {
        CheckoutResponse response = DataStore.SESSIONS.get(id);
        if (updates == null) return null;

        List<LineItem> lineItems = new ArrayList<>();
        if (updates.getItems() != null) {
            for (Item item : updates.getItems()) {
                LineItem lineItem = new LineItem();
                lineItem.setId(UUID.randomUUID().toString());
                lineItem.setItem(item);
                lineItems.add(lineItem);
            }
        }
        response.setLineItems(lineItems);
        response.setStatus(Status.not_ready_for_payment);

        FulfillmentOption fulfillmentOption = new FulfillmentOption();
        response.setFulfillmentOptions(List.of(fulfillmentOption));

        Address address = new Address();
        response.setFulfillmentAddress(address);

        Total total = new Total();
        response.setTotals(List.of(total));

        Link link = new Link();
        response.setLinks(List.of(link));

        return response;
    }

    public CheckoutResponse getSession(String id) {
        return DataStore.SESSIONS.get(id);
    }

    public boolean cancelSession(String id) {
        CheckoutResponse response = DataStore.SESSIONS.get(id);
        if (response == null) return false;
        response.setStatus(Status.canceled);
        DataStore.SESSIONS.put(id, response);
        return true;
    }

    public CheckoutResponse completeSession(String id) {
        CheckoutResponse response = DataStore.SESSIONS.get(id);
        if (response == null) return null;
        response.setBuyer(new Buyer());
        response.setPaymentData(new PaymentData());
        DataStore.SESSIONS.put(id, response);
        return response;
    }

    public boolean idempotencySeen(String idempotencyKey) {
        return DataStore.IDEMPOTENCY.containsKey(idempotencyKey);
    }

    public void recordIdempotency(String idempotencyKey, String sessionId) {
        DataStore.IDEMPOTENCY.put(idempotencyKey, sessionId);
    }
}
