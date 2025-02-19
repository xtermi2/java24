package com.github.xtermi2.java24.jep497mldsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class QuantumResistantModuleLatticeBasedDigitalSignatureAlgorithm {
    public KeyPair generateKeyPair(String algorithmParameterSetName) throws NoSuchAlgorithmException {
        KeyPairGenerator g = KeyPairGenerator.getInstance(algorithmParameterSetName);
        return g.generateKeyPair();
    }
}
