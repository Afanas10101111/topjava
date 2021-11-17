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

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final ValidatorForJdbc<User> validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        validator = new ValidatorForJdbc<>();
    }

    @Override
    @Transactional
    public User save(User user) {
        validator.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        boolean isItAnUpdate = true;
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            isItAnUpdate = false;
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        if (isItAnUpdate) {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.id());
        }
        Set<Role> roles = user.getRoles();
        jdbcTemplate.batchUpdate(
                "INSERT INTO user_roles (user_id, role) VALUES (?, ?)",
                roles,
                roles.size(),
                (ps, argument) -> {
                    ps.setInt(1, user.id());
                    ps.setString(2, argument.name());
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
                "SELECT * FROM users JOIN user_roles ON users.id = user_roles.user_id ORDER BY name, email",
                (rs, rowNum) -> {
                    int id = rs.getInt("id");
                    User user = users.get(id);
                    if (user == null) {
                        user = new User(
                                id,
                                rs.getString("name"),
                                rs.getString("email"),
                                rs.getString("password"),
                                rs.getInt("calories_per_day"),
                                rs.getBoolean("enabled"),
                                rs.getDate("registered"),
                                new HashSet<>()
                        );
                        users.put(id, user);
                    }
                    user.addRoles(Role.valueOf(rs.getString("role")));
                    return user;
                }
        );
        return users.values().stream().toList();
    }

    private User withRoles(User user) {
        Optional.ofNullable(user).ifPresent(
                u -> {
                    List<Role> roles = jdbcTemplate.query(
                            "SELECT * FROM user_roles WHERE user_id=?",
                            (rs, rowNum) -> Role.valueOf(rs.getString("role")),
                            user.id()
                    );
                    user.setRoles(roles);
                }
        );
        return user;
    }
}
