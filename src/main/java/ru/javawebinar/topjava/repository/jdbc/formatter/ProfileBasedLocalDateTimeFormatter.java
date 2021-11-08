package ru.javawebinar.topjava.repository.jdbc.formatter;

import java.time.LocalDateTime;

public interface ProfileBasedLocalDateTimeFormatter<T> {
    T getFormatted(LocalDateTime localDateTime);
}
