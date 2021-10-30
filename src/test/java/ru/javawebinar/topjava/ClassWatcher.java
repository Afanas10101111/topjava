package ru.javawebinar.topjava;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClassWatcher extends TestWatcher {
    public static final Map<String, Long> allTests = new LinkedHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(ClassWatcher.class);

    @Override
    protected void starting(Description description) {
        allTests.clear();
    }

    @Override
    protected void finished(Description description) {
        logger.info("\n****\nSummary\n****");
        allTests.forEach((key, value) -> logger.info("{}: {} ms", key, value));
        logger.info("\n****");
    }
}
