package org.example.planner.team.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.planner.membership.Membership;
import org.example.planner.membership.Role;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamUser {

    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;
    private Role userRole;
    private LocalDate teamJoiningDate;

    public TeamUser(Membership membership) {
        this.userId = membership.getUser().getId();
        this.email = membership.getUser().getEmail();
        this.firstName = membership.getUser().getFirstName();
        this.lastName= membership.getUser().getLastName();
        this.userRole = membership.getRole();
        this.teamJoiningDate = membership.getTeamJoiningDate();
    }
}
