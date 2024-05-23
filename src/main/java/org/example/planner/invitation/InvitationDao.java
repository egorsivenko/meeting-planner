package org.example.planner.invitation;

import org.example.planner.shared.DAO;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InvitationDao implements DAO<Invitation, ICompositeKey> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final InvitationRowMapper rowMapper;

    public InvitationDao(NamedParameterJdbcTemplate jdbcTemplate, InvitationRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Invitation> listAll() {
        String sql = """
                SELECT meeting_id, user_id, status, suggested_time, update_time
                FROM invitations
                ORDER BY meeting_id, user_id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Invitation> getById(ICompositeKey compositeKey) {
        String sql = """
                SELECT meeting_id, user_id, status, suggested_time, update_time
                FROM invitations
                WHERE (meeting_id, user_id) = (:meeting_id, :user_id)
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("meeting_id", compositeKey.meetingId())
                .addValue("user_id", compositeKey.userId());

        return jdbcTemplate.query(sql, params, rowMapper).stream().findFirst();
    }

    @Override
    public ICompositeKey create(Invitation invitation) {
        String sql = """
                INSERT INTO invitations (meeting_id, user_id, status)
                VALUES (:meeting_id, :user_id, :status)
                """;
        ICompositeKey compositeKey = invitation.getCompositeKey();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("meeting_id", compositeKey.meetingId())
                .addValue("user_id", compositeKey.userId())
                .addValue("status", invitation.getStatus().toString());

        jdbcTemplate.update(sql, params);
        return compositeKey;
    }

    @Override
    public void update(Invitation invitation) {
        String sql = """
                UPDATE invitations
                SET status = :status, suggested_time = :suggested_time, update_time = :update_time
                WHERE (meeting_id, user_id) = (:meeting_id, :user_id)
                """;
        ICompositeKey compositeKey = invitation.getCompositeKey();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("status", invitation.getStatus().toString())
                .addValue("suggested_time", invitation.getSuggestedTime())
                .addValue("update_time", invitation.getUpdateTime())
                .addValue("meeting_id", compositeKey.meetingId())
                .addValue("user_id", compositeKey.userId());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public void delete(ICompositeKey compositeKey) {
        String sql = """
                DELETE FROM invitations
                WHERE (meeting_id, user_id) = (:meeting_id, :user_id)
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("meeting_id", compositeKey.meetingId())
                .addValue("user_id", compositeKey.userId());

        jdbcTemplate.update(sql, params);
    }
}
