package com.github.xtermi2.java24.jep491synchronizevirtualthreadswithoutpinning;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class SynchronizeVirtualThreadsWithoutPinningTest {
    private final SynchronizeVirtualThreadsWithoutPinning underTest = new SynchronizeVirtualThreadsWithoutPinning();

    @ParameterizedTest
    @CsvSource({
            "10,45",
            "100,4950",
            "1000,499500",
            "10000,49995000",
            "100000,704982704",
            "1000000,1783293664",
    })
    void runVirtualThreads_performance_test(int numberOfThreads, int expectedResult) {
        final int threadSum = underTest.runVirtualThreads(numberOfThreads);

        assertThat(threadSum)
                .isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({
            "10,45",
            "100,4950",
            "1000,499500",
            "10000,49995000",
            "100000,704982704",
            // now it gets much slower than virtual threads
            //"1000000,1783293664",
    })
    void runPlatformThreads_performance_test(int numberOfThreads, int expectedResult) {
        final int threadSum = underTest.runPlatformThreads(numberOfThreads);

        assertThat(threadSum).isEqualTo(expectedResult);
    }
}