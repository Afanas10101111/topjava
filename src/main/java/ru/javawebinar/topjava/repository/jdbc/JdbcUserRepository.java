package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final ValidatorForJdbc validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, ValidatorForJdbc validator) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.validator = validator;
    }

    @Override
    @Transactional
    public User save(User user) {
        validator.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        } else {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.id());
        }
        Set<Role> roles = user.getRoles();
        jdbcTemplate.batchUpdate(
                "INSERT INTO user_roles (user_id, role) VALUES (?, ?)",
                roles,
                roles.size(),
                (ps, role) -> {
                    ps.setInt(1, user.id());
                    ps.setString(2, role.name());
                }
        );
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return withRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return withRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public List<User> getAll() {
        Map<Integer, User> users = new LinkedHashMap<>();
        jdbcTemplate.query(
                "SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id ORDER BY name, email",
                (rs, rowNum) -> {
                    int id = rs.getInt("id");
                    User user = users.get(id);
                    if (user == null) {
                        user = ROW_MAPPER.mapRow(rs, rowNum);
                        Objects.requireNonNull(user);
                        user.setRoles(new HashSet<>());
                        users.put(id, user);
                    }
                    String role = rs.getString("role");
                    if (role != null) {
                        user.addRoles(Role.valueOf(role));
                    }
                    return user;
                }
        );
        return new ArrayList<>(users.values());
    }

    private User withRoles(User user) {
        if (user != null) {
            List<Role> roles = jdbcTemplate.query(
                    "SELECT * FROM user_roles WHERE user_id=?",
                    (rs, rowNum) -> Role.valueOf(rs.getString("role")),
                    user.id()
            );
            user.setRoles(roles);
        }
        return user;
    }
}
