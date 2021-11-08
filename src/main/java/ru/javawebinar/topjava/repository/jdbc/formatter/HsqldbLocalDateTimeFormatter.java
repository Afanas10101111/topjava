package ru.javawebinar.topjava.repository.jdbc.formatter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class HsqldbLocalDateTimeFormatter implements ProfileBasedLocalDateTimeFormatter<Timestamp>{

    @Override
    public Timestamp getFormatted(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}
