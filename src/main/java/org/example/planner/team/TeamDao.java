package org.example.planner.team;

import org.example.planner.shared.DAO;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class TeamDao implements DAO<Team, Integer> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TeamRowMapper rowMapper;

    public TeamDao(NamedParameterJdbcTemplate jdbcTemplate, TeamRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Team> listAll() {
        String sql = """
                SELECT id, name, description, creation_date
                FROM teams
                ORDER BY id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Team> getById(Integer id) {
        String sql = """
                SELECT id, name, description, creation_date
                FROM teams
                WHERE id = :id
                """;
        return jdbcTemplate.query(sql, Map.of("id", id), rowMapper).stream().findFirst();
    }

    public Optional<Team> getByName(String name) {
        String sql = """
                SELECT id, name, description, creation_date
                FROM teams
                WHERE name = :name
                """;
        return jdbcTemplate.query(sql, Map.of("name", name), rowMapper).stream().findFirst();
    }

    @Override
    public Integer create(Team team) {
        String sql = """
                INSERT INTO teams (name, description, creation_date)
                VALUES (:name, :description, :creation_date)
                RETURNING id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", team.getName())
                .addValue("description", team.getDescription())
                .addValue("creation_date", team.getCreationDate());

        return jdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    @Override
    public void update(Team team) {
        String sql = """
                UPDATE teams
                SET name = :name, description = :description
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", team.getName())
                .addValue("description", team.getDescription())
                .addValue("id", team.getId());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM teams WHERE id = :id";
        jdbcTemplate.update(sql, Map.of("id", id));
    }
}
