package com.agnet.pay.service;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;

/**
 * Utility to sign/verify using RSASSA-PSS SHA256 with MAX_SALT_LENGTH compatibility.
 */
public class HttpSignatureUtils {

    // Use MAX_SALT_LENGTH to match Python/OpenSSL defaults
    private static final PSSParameterSpec PSS_SPEC = new PSSParameterSpec(
            "SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 20, 1);

    public static String signRsaPss(String message, PrivateKey privateKey) throws Exception {
        Signature signer = Signature.getInstance("RSASSA-PSS");
        signer.setParameter(PSS_SPEC);
        signer.initSign(privateKey);
        signer.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] sig = signer.sign();
        return Base64.getEncoder().encodeToString(sig);
    }

    public static boolean verifyRsaPss(String message, String signatureB64, PublicKey publicKey) throws Exception {
        Signature verifier = Signature.getInstance("RSASSA-PSS");
        verifier.setParameter(PSS_SPEC);
        verifier.initVerify(publicKey);
        verifier.update(message.getBytes(StandardCharsets.UTF_8));
        byte[] sig = Base64.getDecoder().decode(signatureB64);
        return verifier.verify(sig);
    }
}