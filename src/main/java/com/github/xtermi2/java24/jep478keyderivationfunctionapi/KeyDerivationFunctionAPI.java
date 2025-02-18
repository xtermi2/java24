package com.github.xtermi2.java24.jep478keyderivationfunctionapi;

import javax.crypto.KDF;
import javax.crypto.SecretKey;
import javax.crypto.spec.HKDFParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

public class KeyDerivationFunctionAPI {
    void main() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        // Create a KDF object for the specified algorithm
        KDF hkdf = KDF.getInstance("HKDF-SHA256");

        // Create an ExtractExpand parameter specification
        AlgorithmParameterSpec params =
                HKDFParameterSpec.ofExtract()
                        .addIKM("initialKeyMaterial".getBytes())
                        .addSalt("salt".getBytes())
                        .thenExpand("info".getBytes(), 32);

        // Derive a 32-byte AES key
        SecretKey key = hkdf.deriveKey("AES", params);

        System.out.println("Algorithm: " + key.getAlgorithm());
        System.out.println("Format: " + key.getFormat());
        System.out.println("Key base64 encoded: " + new String(Base64.getEncoder().encode(key.getEncoded())));

        // Additional deriveKey calls can be made with the same KDF object
    }
}
