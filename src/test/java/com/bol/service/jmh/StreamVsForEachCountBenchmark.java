package com.bol.service.jmh;

import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamVsForEachCountBenchmark {

    @Test
    public void benchmark() throws Exception {
        StreamVsForEachCountBenchmark orcaJmhBenchmark = new StreamVsForEachCountBenchmark();
        orcaJmhBenchmark.runJmh();
    }

    public void runJmh() throws Exception {
        Options opt = new OptionsBuilder()
                .include(getClass().getSimpleName())
                .forks(1)
                .threads(16)
                //.jvmArgs("-Dorca.port=" + port)
                .build();

        Collection<RunResult> results = new Runner(opt).run();
    }

    static final List<Integer> OBJECTS =
            IntStream
                    .range(0, 100)
                    .boxed()
                    .collect(Collectors.toList());

    static final int[] ARRAY = new int[OBJECTS.size()];
    static {
        for (int i = 0; i < OBJECTS.size(); i++) ARRAY[i] = OBJECTS.get(i);
    }

    static final AtomicLong atomicLong = new AtomicLong();

    void foo(int i, int j) {
        // to avoid compiler optimizing this out
        if (j - i > 0) atomicLong.get();
    }

    @Benchmark
    public void stream() {
        OBJECTS.stream().forEach(i -> foo(i, i + 1));
    }

    @Benchmark
    public void noStream() {
        OBJECTS.forEach(i -> foo(i, i + 1));
    }

    @Benchmark
    public void foreachListiterator() {
        OBJECTS.iterator().forEachRemaining(i -> foo(i, i + 1));
    }

    @Benchmark
    public void foreach() {
        for (Integer i : OBJECTS) foo(i, i + 1);
    }

    @Benchmark
    public void foreachArray() {
        for (int i : ARRAY) foo(i, i + 1);
    }

    @Benchmark
    public void forArray() {
        for (int i = 0; i < ARRAY.length; i++) {
            int i1 = ARRAY[i];
            foo(i1, i1 + 1);
        }
    }
}
