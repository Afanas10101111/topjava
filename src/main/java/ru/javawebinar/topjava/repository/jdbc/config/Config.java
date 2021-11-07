package ru.javawebinar.topjava.repository.jdbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.javawebinar.topjava.Profiles;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Configuration
public class Config {

    @Bean
    @Profile(Profiles.HSQL_DB)
    public ProfileBasedLocalDateTimeFormatter<Timestamp> getFormatterForHsqldb() {
        return Timestamp::valueOf;
    }

    @Bean
    @Profile(Profiles.POSTGRES_DB)
    public ProfileBasedLocalDateTimeFormatter<LocalDateTime> getFormatterForPostgres() {
        return localDateTime -> localDateTime;
    }
}
