package com.github.xtermi2.java24.jep496mlkem;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.NamedParameterSpec;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class QuantumResistantModuleLatticeBasedKeyEncapsulationMechanismTest {

    private final QuantumResistantModuleLatticeBasedKeyEncapsulationMechanism underTest = new QuantumResistantModuleLatticeBasedKeyEncapsulationMechanism();

    @ParameterizedTest
    @ValueSource(strings = {"ML-KEM-512", "ML-KEM-768", "ML-KEM-1024"})
    void generate_ML_KEM_KeyPair(String parameterSpec) throws NoSuchAlgorithmException {
        final KeyPair keyPair = underTest.generateKeyPair(parameterSpec);

        assertSoftly(softly -> {
            // validate private key
            softly.assertThat(keyPair.getPrivate().getAlgorithm())
                    .as("private key algorithm")
                    .isEqualTo("ML-KEM");
            softly.assertThat(keyPair.getPrivate().getFormat())
                    .as("private key format")
                    .isEqualTo("PKCS#8");
            softly.assertThat(keyPair.getPrivate().getParams())
                    .as("private key params")
                    .isInstanceOf(NamedParameterSpec.class)
                    .hasFieldOrPropertyWithValue("name", parameterSpec);

            // validate public key
            softly.assertThat(keyPair.getPublic().getAlgorithm())
                    .as("public key algorithm")
                    .isEqualTo("ML-KEM");
            softly.assertThat(keyPair.getPublic().getFormat())
                    .as("public key format")
                    .isEqualTo("X.509");
            softly.assertThat(keyPair.getPublic().getParams())
                    .as("public key params")
                    .isInstanceOf(NamedParameterSpec.class)
                    .hasFieldOrPropertyWithValue("name", parameterSpec);
        });

    }
}