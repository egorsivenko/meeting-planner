package org.example.planner.user;

import org.example.planner.shared.DAO;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserDao implements DAO<User, Integer> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserRowMapper rowMapper;

    public UserDao(NamedParameterJdbcTemplate jdbcTemplate, UserRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<User> listAll() {
        String sql = """
                SELECT id, email, password, first_name, last_name, registration_date
                FROM users
                ORDER BY id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<User> getById(Integer id) {
        String sql = """
                SELECT id, email, password, first_name, last_name, registration_date
                FROM users
                WHERE id = :id
                """;
        return jdbcTemplate.queryForStream(sql, Map.of("id", id), rowMapper).findFirst();
    }

    @Override
    public Integer create(User user) {
        String sql = """
                INSERT INTO users (email, password, first_name, last_name, registration_date)
                VALUES (:email, :password, :first_name, :last_name, :registration_date)
                RETURNING id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("first_name", user.getFirstName())
                .addValue("last_name", user.getLastName())
                .addValue("registration_date", user.getRegistrationDate());

        return jdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    @Override
    public void update(User user) {
        String sql = """
                UPDATE users
                SET email = :email, password = :password,
                    first_name = :first_name, last_name = :last_name
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("first_name", user.getFirstName())
                .addValue("last_name", user.getLastName())
                .addValue("id", user.getId());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM users WHERE id = :id";
        jdbcTemplate.update(sql, Map.of("id", id));
    }
}
