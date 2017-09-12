package com.bol.service.jmh;

import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamVsForEachListBenchmark {

    @Test
    public void benchmark() throws Exception {
        StreamVsForEachListBenchmark orcaJmhBenchmark = new StreamVsForEachListBenchmark();
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

    private static final List<Integer> OBJECTS =
            IntStream
                    .range(0, 100)
                    .boxed()
                    .collect(Collectors.toList());

    private void foo(Integer i, List<Integer> list) {
        list.add(i);
    }

    @Benchmark
    public void stream(Blackhole blackhole) {
        List<Integer> list = new ArrayList<>(127);
        OBJECTS.stream().forEach(i -> foo(i, list));
        blackhole.consume(list);
    }

    @Benchmark
    public void noStream(Blackhole blackhole) {
        List<Integer> list = new ArrayList<>(127);
        OBJECTS.forEach(i -> foo(i, list));
        blackhole.consume(list);
    }

    @Benchmark
    public void foreach(Blackhole blackhole) {
        List<Integer> list = new ArrayList<>(127);
        OBJECTS.iterator().forEachRemaining(i -> foo(i, list));
        blackhole.consume(list);
    }
}
