package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

public class FormattingService extends FormattingConversionServiceFactoryBean {
    public FormattingService(String dateFormatPattern, String timeFormatPattern) {
        this.setFormatters(Set.of(
                new Formatter<LocalDate>() {
                    @Override
                    public LocalDate parse(String text, Locale locale) {
                        return LocalDate.parse(text, DateTimeFormatter.ofPattern(dateFormatPattern));
                    }

                    @Override
                    public String print(LocalDate date, Locale locale) {
                        return date.toString();
                    }
                },
                new Formatter<LocalTime>() {
                    @Override
                    public LocalTime parse(String text, Locale locale) {
                        return LocalTime.parse(text, DateTimeFormatter.ofPattern(timeFormatPattern));
                    }

                    @Override
                    public String print(LocalTime date, Locale locale) {
                        return date.toString();
                    }
                }
        ));
    }
}
