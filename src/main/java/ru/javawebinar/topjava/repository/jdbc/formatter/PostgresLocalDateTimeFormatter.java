package ru.javawebinar.topjava.repository.jdbc.formatter;

import java.time.LocalDateTime;

public class PostgresLocalDateTimeFormatter implements ProfileBasedLocalDateTimeFormatter<LocalDateTime>{

    @Override
    public LocalDateTime getFormatted(LocalDateTime localDateTime) {
        return localDateTime;
    }
}
