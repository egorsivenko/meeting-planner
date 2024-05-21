package org.example.planner.meeting;

import org.example.planner.team.Team;
import org.example.planner.user.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class MeetingRowMapper implements RowMapper<Meeting> {

    @Override
    public Meeting mapRow(ResultSet rs, int rowNum) throws SQLException {
        User organizer = User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .registrationDate(rs.getDate("registration_date").toLocalDate())
                .build();

        Team team = Team.builder()
                .id(rs.getInt("team_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .creationDate(rs.getDate("creation_date").toLocalDate())
                .build();

        LocalDateTime updateTime = Optional.ofNullable(rs.getTimestamp("update_time"))
                .map(Timestamp::toLocalDateTime).orElse(null);

        return Meeting.builder()
                .id(rs.getInt("meeting_id"))
                .organizer(organizer)
                .team(team)
                .subject(rs.getString("subject"))
                .startTime(rs.getTimestamp("start_time").toLocalDateTime())
                .endTime(rs.getTimestamp("end_time").toLocalDateTime())
                .link(rs.getString("link"))
                .status(MeetingStatus.valueOf(rs.getString("status")))
                .creationTime(rs.getTimestamp("creation_time").toLocalDateTime())
                .updateTime(updateTime)
                .build();
    }
}
