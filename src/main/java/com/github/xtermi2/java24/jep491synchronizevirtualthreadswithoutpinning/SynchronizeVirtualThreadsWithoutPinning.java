package com.github.xtermi2.java24.jep491synchronizevirtualthreadswithoutpinning;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class SynchronizeVirtualThreadsWithoutPinning {

    /**
     * run with new virtual threads
     */
    public int runVirtualThreads(int numberOfThreads) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            return runInThreads(numberOfThreads, executor);
        }
    }

    /**
     * run with classic threads
     */
    public int runPlatformThreads(int numberOfThreads) {
        try (var executor = Executors.newCachedThreadPool()) {
            return runInThreads(numberOfThreads, executor);
        }
    }

    private Integer runInThreads(int numberOfThreads, ExecutorService executor) {
        final List<Future<Integer>> futures = IntStream.range(0, numberOfThreads)
                .mapToObj(i -> executor.submit(() -> runInThread(i))).toList();
        return futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }).reduce(Integer::sum)
                .orElse(0);
    }

    private int runInThread(final Integer i) {
        synchronized (i) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return i;
        }
    }
}
