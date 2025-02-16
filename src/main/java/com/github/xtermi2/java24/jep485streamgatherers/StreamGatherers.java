package com.github.xtermi2.java24.jep485streamgatherers;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

/**
 * A stream pipeline consists of three parts:
 * 1. a source of elements
 * 2. any number of intermediate operations
 * 3. and a terminal operation
 * <p>
 * now with Gatherers, it is possible to build custom intermediate operations. This is comparable with the Collector in the terminal operation
 * <p>
 * When invoked, Stream::gather performs the equivalent of the following steps:
 * - Create a Downstream object which, when given an element of the gatherer’s output type, passes it to the next stage in the pipeline.
 * - Obtain the gatherer’s private state object by invoking the get() method of its initializer.
 * - Obtain the gatherer’s integrator by invoking its integrator() method.
 * - While there are more input elements, invoke the integrator's integrate(...) method, passing it the state object, the next element, and the downstream object. Terminate if that method returns false.
 * - Obtain the gatherer’s finisher and invoke it with the state and downstream objects.
 * <p>
 * Built-in gatherers see {@link java.util.stream.Gatherers}
 */
public class StreamGatherers {
    public record Reading(Instant obtainedAt, int kelvins) {
        Reading(String time, int kelvins) {
            this(Instant.parse(time), kelvins);
        }
    }

    boolean isSuspicious(Reading previous, Reading next) {
        return next.obtainedAt().isBefore(previous.obtainedAt().plusSeconds(5))
                && (next.kelvins() > previous.kelvins() + 30
                || next.kelvins() < previous.kelvins() - 30);
    }

    List<List<Reading>> findSuspicious(Stream<Reading> source) {
        return source.gather(Gatherers.windowSliding(2))
                .filter(window -> (window.size() == 2
                        && isSuspicious(window.get(0),
                        window.get(1))))
                .toList();
    }

    public static Gatherer<Number, AtomicReference<Number>, Number> max(Number minDefault) {
        return Gatherer.ofSequential(
                // initializer
                () -> new AtomicReference<>(minDefault),

                // integrator
                (state, element, downstream) -> {
                    if (state.get().doubleValue() < element.doubleValue()) {
                        state.set(element);
                    }
                    return true;
                },

                // finisher
                (state, downstream) -> downstream.push(state.get()));
    }
}
