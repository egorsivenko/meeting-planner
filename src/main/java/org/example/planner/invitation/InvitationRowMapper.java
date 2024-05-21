package org.example.planner.invitation;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class InvitationRowMapper implements RowMapper<Invitation> {

    @Override
    public Invitation mapRow(ResultSet rs, int rowNum) throws SQLException {
        ICompositeKey compositeKey = new ICompositeKey(
                rs.getInt("meeting_id"),
                rs.getInt("user_id")
        );

        InvitationStatus status = InvitationStatus.valueOf(rs.getString("status"));

        LocalDateTime suggestedTime = Optional.ofNullable(rs.getTimestamp("suggested_time"))
                .map(Timestamp::toLocalDateTime).orElse(null);

        LocalDateTime updateTime = Optional.ofNullable(rs.getTimestamp("update_time"))
                .map(Timestamp::toLocalDateTime).orElse(null);

        return Invitation.builder()
                .compositeKey(compositeKey)
                .status(status)
                .suggestedTime(suggestedTime)
                .updateTime(updateTime)
                .build();
    }
}
