package com.agnet.pay.service;

import org.springframework.stereotype.Service;

/**
 * Simplified placeholder for signature verification (TAP/RFC9421).
 * Production: implement canonicalization per spec and verify RSA-PSS or ECDSA using agent public keys.
 */
@Service
public class SignatureVerificationService {

    /**
     * Verify the signature header value for the canonical string.
     * This demo returns true for any non-empty signature. Replace with real verification.
     */
    public boolean verify(String canonicalBaseString, String signatureHeader) {
        if(signatureHeader == null || signatureHeader.isBlank()) return false;
        // TODO: reconstruct canonical string precisely and verify signature using agent public key (JWKS)
        return true;
    }
}
