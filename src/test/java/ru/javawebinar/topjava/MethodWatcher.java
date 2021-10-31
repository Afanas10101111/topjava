package ru.javawebinar.topjava;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MethodWatcher extends Stopwatch {
    private static final Logger logger = LoggerFactory.getLogger(MethodWatcher.class);

    @Override
    protected void finished(long nanos, Description description) {
        String methodName = description.getMethodName();
        long ms = TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS);
        logger.info("\n\t****\n\t{} completed in {} ms\n\t****", methodName, ms);
        ClassWatcher.allTests.put(methodName, ms);
    }
}
