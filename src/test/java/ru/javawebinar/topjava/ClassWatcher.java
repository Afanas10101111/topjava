package ru.javawebinar.topjava;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassWatcher extends TestWatcher {
    public static final StringBuilder results = new StringBuilder();

    private static final Logger log = LoggerFactory.getLogger("result");

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
