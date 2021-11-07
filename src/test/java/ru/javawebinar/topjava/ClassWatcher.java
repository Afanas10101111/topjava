package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ClassWatcher extends TestWatcher {
    private static final Logger log = LoggerFactory.getLogger("result");

    private final StringBuilder results;
    private final Stopwatch methodWatcher;

    public ClassWatcher() {
        results = new StringBuilder();
        methodWatcher = new Stopwatch() {

            @Override
            protected void finished(long nanos, Description description) {
                String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
                results.append(result);
                log.info(result + " ms\n");
            }
        };
    }

    public Stopwatch getMethodWatcher() {
        return methodWatcher;
    }

    @Override
    protected void starting(Description description) {
        results.setLength(0);
    }

    @Override
    protected void finished(Description description) {
        log.info("""
                ---------------------------------
                Test                 Duration, ms
                ---------------------------------"""
                + results +
                "\n---------------------------------");
    }
}
