package com.github.xtermi2.java24.jep485streamgatherers;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.github.xtermi2.java24.jep485streamgatherers.StreamGatherers.max;
import static org.assertj.core.api.Assertions.assertThat;

class StreamGatherersTest {

    StreamGatherers.Reading _1 = new StreamGatherers.Reading("2023-09-21T10:15:30.00Z", 310);
    StreamGatherers.Reading _2 = new StreamGatherers.Reading("2023-09-21T10:15:31.00Z", 312);
    StreamGatherers.Reading _3 = new StreamGatherers.Reading("2023-09-21T10:15:32.00Z", 350);
    StreamGatherers.Reading _4 = new StreamGatherers.Reading("2023-09-21T10:15:33.00Z", 310);
    Stream<StreamGatherers.Reading> recentReadings = Stream.of(_1, _2, _3, _4);

    @Test
    void findSuspicious() {
        final List<List<StreamGatherers.Reading>> res = new StreamGatherers().findSuspicious(recentReadings);

        assertThat(res)
                .containsExactly(
                        List.of(_2, _3),
                        List.of(_3, _4)
                );
    }

    @Test
    void max_with_custom_minDefault_higher_max_in_elements() {
        final List<Number> res = IntStream.of(1, 2, 3, 4, 5)
                .boxed()
                .gather(max(6))
                .toList();

        assertThat(res)
                .containsExactly(6);
    }

    @Test
    void max_with_custom_minDefault_lower_max_in_elements() {
        final List<Number> res = IntStream.of(1, 2, 3, 4, 5)
                .boxed()
                .gather(max(4))
                .toList();

        assertThat(res)
                .containsExactly(5);
    }
}