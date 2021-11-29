package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomTimeFormatter implements Formatter<LocalTime> {
    private final String timeFormatPattern;

    public CustomTimeFormatter(String timeFormatPattern) {
        this.timeFormatPattern = timeFormatPattern;
    }

    @Override
    public LocalTime parse(String text, Locale locale) {
        return LocalTime.parse(text, DateTimeFormatter.ofPattern(timeFormatPattern));
    }

    @Override
    public String print(LocalTime time, Locale locale) {
        return time.toString();
    }
}
