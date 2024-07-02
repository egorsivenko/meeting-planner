package org.example.planner.team;

import org.example.planner.membership.MembershipService;
import org.example.planner.membership.Role;
import org.example.planner.team.exception.UserTeamsLimitExceededException;
import org.example.planner.team.exception.NoTeamFoundByNameException;
import org.example.planner.team.exception.TeamIsFullException;
import org.example.planner.team.exception.TeamNameAlreadyExistsException;
import org.example.planner.team.form.CreateTeamForm;
import org.example.planner.team.form.JoinTeamForm;
import org.example.planner.team.form.UpdateTeamForm;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TeamService {

    private static final int USER_TEAMS_LIMIT = 100;
    private static final int TEAM_MEMBERS_LIMIT = 150;

    private final TeamDao teamDao;
    private final MembershipService membershipService;

    public TeamService(TeamDao teamDao, MembershipService membershipService) {
        this.teamDao = teamDao;
        this.membershipService = membershipService;
    }

    public Team getTeamById(Integer id) {
        return teamDao.getById(id).orElseThrow();
    }

    public void createTeam(CreateTeamForm createTeamForm) {
        String name = createTeamForm.getName().strip();

        if (teamDao.getByName(name).isPresent()) {
            throw new TeamNameAlreadyExistsException(name);
        }
        verifyUserTeamsLimit();

        Team team = Team.builder()
                .name(name)
                .description(createTeamForm.getDescription().strip())
                .creationDate(LocalDate.now())
                .build();

        Integer teamId = teamDao.create(team);
        team.setId(teamId);

        membershipService.addUserToTeam(team, Role.OWNER);
    }

    public void joinTeam(JoinTeamForm joinTeamForm) {
        String teamName = joinTeamForm.getTeamName().strip();
        Optional<Team> teamOptional = teamDao.getByName(teamName);

        if (teamOptional.isEmpty()) {
            throw new NoTeamFoundByNameException(teamName);
        }
        verifyUserTeamsLimit();

        Team team = teamOptional.orElseThrow();
        int teamId = team.getId();

        verifyTeamMembersLimit(teamId);
        try {
            membershipService.getUserTeam(teamId);
            throw new IllegalArgumentException("You are already a member of this team");
        } catch (NoSuchElementException ex) {
            membershipService.addUserToTeam(team, Role.MEMBER);
        }
    }

    public void updateTeam(UpdateTeamForm updateTeamForm) {
        String name = updateTeamForm.getName().strip();
        Team team = teamDao.getById(updateTeamForm.getId()).orElseThrow();

        if (!team.getName().equals(name) && teamDao.getByName(name).isPresent()) {
            throw new TeamNameAlreadyExistsException(name);
        }
        team.setName(updateTeamForm.getName());
        team.setDescription(updateTeamForm.getDescription().strip());

        teamDao.update(team);
    }

    public void deleteTeam(Integer teamId) {
        teamDao.delete(teamId);
    }

    private void verifyUserTeamsLimit() {
        int userTeamsNumber = membershipService.getUserTeams().size();

        if (userTeamsNumber >= USER_TEAMS_LIMIT) {
            throw new UserTeamsLimitExceededException();
        }
    }

    private void verifyTeamMembersLimit(int teamId) {
        int teamMembersNumber = membershipService.getTeamUsers(teamId).size();

        if (teamMembersNumber >= TEAM_MEMBERS_LIMIT) {
            throw new TeamIsFullException();
        }
    }
}
