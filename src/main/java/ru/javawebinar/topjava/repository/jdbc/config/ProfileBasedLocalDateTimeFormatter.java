package ru.javawebinar.topjava.repository.jdbc.config;

import java.time.LocalDateTime;

public interface ProfileBasedLocalDateTimeFormatter<T> {
    T getFormatted(LocalDateTime localDateTime);
}
