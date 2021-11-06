package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MethodWatcher extends Stopwatch {
    private static final Logger log = LoggerFactory.getLogger("result");

    @Override
    protected void finished(long nanos, Description description) {
        String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
        ClassWatcher.results.append(result);
        log.info(result + " ms\n");
    }
}
