package org.example.planner.membership;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MembershipRowMapper implements RowMapper<Membership> {

    @Override
    public Membership mapRow(ResultSet rs, int rowNum) throws SQLException {
        MCompositeKey compositeKey = new MCompositeKey(
                rs.getInt("team_id"),
                rs.getInt("user_id")
        );

        return Membership.builder()
                .compositeKey(compositeKey)
                .role(Role.valueOf(rs.getString("role")))
                .teamJoiningDate(rs.getDate("team_joining_date").toLocalDate())
                .roleAssignmentDate(rs.getDate("role_assignment_date").toLocalDate())
                .build();
    }
}
