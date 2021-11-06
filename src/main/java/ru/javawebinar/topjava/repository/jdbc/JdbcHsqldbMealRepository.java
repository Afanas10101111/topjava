package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Repository
@Profile(Profiles.HSQL_DB)
public class JdbcHsqldbMealRepository extends JdbcMealRepository {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public JdbcHsqldbMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
        clazz = String.class;
    }

    @Override
    protected <T> T formattedDateTime(LocalDateTime localDateTime, Class<T> clazz) {
        return clazz.cast(localDateTime.format(FORMATTER));
    }
}
