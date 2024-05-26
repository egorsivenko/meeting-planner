package org.example.planner.membership;

import org.example.planner.shared.DAO;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MembershipDao implements DAO<Membership, MCompositeKey> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final MembershipRowMapper rowMapper;

    public MembershipDao(NamedParameterJdbcTemplate jdbcTemplate, MembershipRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Membership> listAll() {
        String sql = """
                SELECT team_id, t.name, t.description, t.creation_date,
                       user_id, u.email, u.password, u.first_name, u.last_name, u.registration_date,
                       m.role, m.team_joining_date, m.role_assignment_date
                FROM memberships m
                JOIN teams t
                    ON m.team_id = t.id
                JOIN users u
                    ON m.user_id = u.id
                ORDER BY team_id, user_id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Membership> getById(MCompositeKey compositeKey) {
        String sql = """
                SELECT team_id, t.name, t.description, t.creation_date,
                       user_id, u.email, u.password, u.first_name, u.last_name, u.registration_date,
                       m.role, m.team_joining_date, m.role_assignment_date
                FROM memberships m
                JOIN teams t
                    ON m.team_id = t.id
                JOIN users u
                    ON m.user_id = u.id
                WHERE (team_id, user_id) = (:team_id, :user_id)
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("team_id", compositeKey.teamId())
                .addValue("user_id", compositeKey.userId());

        return jdbcTemplate.query(sql, params, rowMapper).stream().findFirst();
    }

    @Override
    public MCompositeKey create(Membership membership) {
        String sql = """
                INSERT INTO memberships (team_id, user_id, role, team_joining_date, role_assignment_date)
                VALUES (:team_id, :user_id, :role::user_role, :team_joining_date, :role_assignment_date)
                """;
        Integer teamId = membership.getTeam().getId();
        Integer userId = membership.getUser().getId();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("team_id", teamId)
                .addValue("user_id", userId)
                .addValue("role", membership.getRole().toString())
                .addValue("team_joining_date", membership.getTeamJoiningDate())
                .addValue("role_assignment_date", membership.getRoleAssignmentDate());

        jdbcTemplate.update(sql, params);
        return new MCompositeKey(teamId, userId);
    }

    @Override
    public void update(Membership membership) {
        String sql = """
                UPDATE memberships
                SET role = :role::user_role, role_assignment_date = :role_assignment_date
                WHERE (team_id, user_id) = (:team_id, :user_id)
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("role", membership.getRole().toString())
                .addValue("role_assignment_date", membership.getRoleAssignmentDate())
                .addValue("team_id", membership.getTeam().getId())
                .addValue("user_id", membership.getUser().getId());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public void delete(MCompositeKey compositeKey) {
        String sql = """
                DELETE FROM memberships
                WHERE (team_id, user_id) = (:team_id, :user_id)
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("team_id", compositeKey.teamId())
                .addValue("user_id", compositeKey.userId());

        jdbcTemplate.update(sql, params);
    }
}
