package com.github.xtermi2.java24.jep487scopedvalues;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScopedValueServerTest {

    ScopedValueServer underTest = new ScopedValueServer();

    @Test
    void scopedValue_should_not_be_visible_outside_of_the_scope() {
        underTest.serve("req", "res", "admin");

        assertThatThrownBy(ScopedValueServer.SECURITY_CONTEXT::get)
                .isInstanceOf(NoSuchElementException.class);
    }
}