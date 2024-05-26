package org.example.planner.membership;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.planner.team.Team;
import org.example.planner.user.User;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Membership {

    private Team team;
    private User user;
    private Role role;
    private LocalDate teamJoiningDate;
    private LocalDate roleAssignmentDate;
}
