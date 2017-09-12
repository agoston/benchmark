package com.bol.service.jmh;

import org.junit.Test;

public class RunBenchmark {

    @Test
    public void benchmark() throws Exception {
        JmhBenchmark orcaJmhBenchmark = new JmhBenchmark();
        orcaJmhBenchmark.runJmh();
    }

}
