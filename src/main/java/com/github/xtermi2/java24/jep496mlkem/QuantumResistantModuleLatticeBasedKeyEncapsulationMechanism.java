package com.github.xtermi2.java24.jep496mlkem;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class QuantumResistantModuleLatticeBasedKeyEncapsulationMechanism {
    public KeyPair generateKeyPair(String algorithmParameterSetName) throws NoSuchAlgorithmException {
        KeyPairGenerator g = KeyPairGenerator.getInstance(algorithmParameterSetName);
        return g.generateKeyPair();
    }
}
