package com.agnet.pay.service;

import com.agnet.pay.configurations.DataStore;
import com.agnet.pay.dto.payment.CardNumberType;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Issue delegated token (JWT) signed by PSP keypair.
 */
@Slf4j
@Service
public class TokenService {

    public String issueDelegatedToken(String id, String checkoutSessionId, long maxAmount, String currency, String merchantId, long ttlSeconds, CardNumberType type, String number, String expireMonth, String expireYear) throws Exception {
        Instant now = Instant.now();
        JWTClaimsSet.Builder cb = new JWTClaimsSet.Builder();
        cb.issuer("fiserv");
        cb.subject(id);
        cb.audience(merchantId);
        cb.issueTime(Date.from(now));
        cb.expirationTime(Date.from(now.plusSeconds(ttlSeconds)));
        cb.claim("scope", Map.of(
                "max_amount", maxAmount,
                "currency", currency,
                "checkout_session_id", checkoutSessionId,
                "merchant_id", merchantId,
                "cardNumberType", type,
                "number", number,
                "expireMonth", expireMonth,
                "expireYear", expireYear
        ));
        SignedJWT sj = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), cb.build());
        JWSSigner signer = new RSASSASigner(DataStore.PSP_KEYPAIR.getPrivate());
        sj.sign(signer);
        return sj.serialize();
    }

    public boolean validateDelegatedToken(String token, String merchantId, long amountCents, String checkoutSessionId) {
        try {
            SignedJWT sj = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier((java.security.interfaces.RSAPublicKey) DataStore.PSP_KEYPAIR.getPublic());
            if (!sj.verify(verifier)) return false;
            var claims = sj.getJWTClaimsSet();
            if (!claims.getAudience().contains(merchantId)) return false;
            Map<String, Object> scope = (Map<String, Object>) claims.getClaim("scope");
            long max = ((Number) scope.get("max_amount")).longValue();
            String cs = (String) scope.get("checkout_session_id");
            return amountCents <= max && checkoutSessionId.equals(cs) && claims.getExpirationTime().after(Date.from(Instant.now()));
        } catch (Exception e) {
            log.info("Invalid token {}", e.getMessage());
            return false;
        }
    }
}
