package org.example.planner.membership;

import org.example.planner.team.Team;
import org.example.planner.user.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MembershipRowMapper implements RowMapper<Membership> {

    @Override
    public Membership mapRow(ResultSet rs, int rowNum) throws SQLException {
        Team team = Team.builder()
                .id(rs.getInt("team_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .creationDate(rs.getDate("creation_date").toLocalDate())
                .build();

        User user = User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .password(rs.getString("password"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .registrationDate(rs.getDate("registration_date").toLocalDate())
                .build();

        return Membership.builder()
                .team(team)
                .user(user)
                .role(Role.valueOf(rs.getString("role")))
                .teamJoiningDate(rs.getDate("team_joining_date").toLocalDate())
                .roleAssignmentDate(rs.getDate("role_assignment_date").toLocalDate())
                .build();
    }
}
