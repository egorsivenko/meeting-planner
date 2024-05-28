package org.example.planner.team.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.planner.membership.Membership;
import org.example.planner.membership.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTeam {

    private Integer teamId;
    private String teamName;
    private String teamDescription;
    private Role userRole;

    public UserTeam(Membership membership) {
        this.teamId = membership.getTeam().getId();
        this.teamName = membership.getTeam().getName();
        this.teamDescription = membership.getTeam().getDescription();
        this.userRole = membership.getRole();
    }
}
