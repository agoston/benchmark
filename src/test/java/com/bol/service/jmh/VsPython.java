package com.bol.service.jmh;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VsPython {

    @Test
    public void benchmark() throws Exception {
        VsPython orcaJmhBenchmark = new VsPython();
        orcaJmhBenchmark.runJmh();
    }

    public void runJmh() throws Exception {
        Options opt = new OptionsBuilder()
                .include(getClass().getSimpleName())
                .forks(1)
                .threads(1)
                //.jvmArgs("-Dorca.port=" + port)
                .build();

        Collection<RunResult> results = new Runner(opt).run();
    }

//    def f(x):
//    back = []
//            for i in range(0,x):
//            if i not in back:
//            back.append(i)
//            return back[1:10]

    static int res;

    public static List<Integer> f(int x) {
        List<Integer> back = new ArrayList<>(x);

        for (int i = 0; i < x; i++)
            if (!back.contains(i)) back.add(i);

        return back.subList(0, 10);
    }

    public static int[] f2(int x) {
        int[] back = new int[x];
        int backi = 0;

        for (int i = 0; i < x; i++)
            if (!ArrayUtils.contains(back, i)) back[backi++] = i;

        return ArrayUtils.subarray(back, 0, 10);
    }

    @Benchmark
    public void one() {
        res = f(20000).get(0);
    }

    @Benchmark
    public void two() {
        res = f(20000).get(0) + f(20000).get(1);
    }

    @Benchmark
    public void one2() {
        res = f2(20000)[0];
    }

    @Benchmark
    public void two2() {
        res = f2(20000)[0] + f2(20000)[1];
    }
}
