package ru.javawebinar.topjava;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class MethodWatcher extends TestWatcher {
    private static final Logger logger = LoggerFactory.getLogger(MethodWatcher.class);

    private long start;

    @Override
    protected void starting(Description description) {
        start = new Date().getTime();
    }

    @Override
    protected void finished(Description description) {
        long ms = (new Date()).getTime() - start;
        String classMethodName = description.getTestClass().getName() + "::" + description.getMethodName();
        logger.info("\n****\n{} completed in {} ms\n****", classMethodName, ms);
        ClassWatcher.allTests.put(classMethodName, ms);
    }
}
