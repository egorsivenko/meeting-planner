package org.example.planner.meeting;

import org.example.planner.shared.DAO;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MeetingDao implements DAO<Meeting, Integer> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final MeetingRowMapper rowMapper;

    public MeetingDao(NamedParameterJdbcTemplate jdbcTemplate, MeetingRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Meeting> listAll() {
        String sql = """
                SELECT m.id AS meeting_id, m.subject, m.start_time, m.end_time,
                            m.link, m.status, m.creation_time, m.update_time,
                       u.id AS user_id, u.email, u.password, u.first_name, u.last_name,
                            u.registration_date,
                       t.id AS team_id, t.name, t.description, t.creation_date
                FROM meetings m
                JOIN users u
                    ON m.organizer_id = u.id
                JOIN teams t
                    ON m.team_id = t.id
                ORDER BY meeting_id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Meeting> getById(Integer id) {
        String sql = """
                SELECT m.id AS meeting_id, m.subject, m.start_time, m.end_time,
                            m.link, m.status, m.creation_time, m.update_time,
                       u.id AS user_id, u.email, u.password, u.first_name, u.last_name,
                            u.registration_date,
                       t.id AS team_id, t.name, t.description, t.creation_date
                FROM meetings m
                JOIN users u
                    ON m.organizer_id = u.id
                JOIN teams t
                    ON m.team_id = t.id
                WHERE m.id = :id
                """;
        return jdbcTemplate.queryForStream(sql, Map.of("id", id), rowMapper).findFirst();
    }

    @Override
    public Integer create(Meeting meeting) {
        String sql = """
                INSERT INTO meetings (organizer_id, team_id, subject, start_time, end_time,
                                      link, status, creation_time)
                VALUES (:organizer_id, :team_id, :subject, :start_time, :end_time,
                        :link, :status, :creation_time)
                RETURNING id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("organizer_id", meeting.getOrganizer().getId())
                .addValue("team_id", meeting.getTeam().getId())
                .addValue("subject", meeting.getSubject())
                .addValue("start_time", meeting.getStartTime())
                .addValue("end_time", meeting.getEndTime())
                .addValue("link", meeting.getLink())
                .addValue("status", meeting.getStatus().toString())
                .addValue("creation_time", meeting.getCreationTime());

        return jdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    @Override
    public void update(Meeting meeting) {
        String sql = """
                UPDATE meetings
                SET subject = :subject, start_time = :start_time, end_time = :end_time,
                    link = :link, status = :status, update_time = :update_time
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("subject", meeting.getSubject())
                .addValue("start_time", meeting.getStartTime())
                .addValue("end_time", meeting.getEndTime())
                .addValue("link", meeting.getLink())
                .addValue("status", meeting.getStatus().toString())
                .addValue("update_time", meeting.getUpdateTime())
                .addValue("id", meeting.getId());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM meetings WHERE id = :id";
        jdbcTemplate.update(sql, Map.of("id", id));
    }
}
