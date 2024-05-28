package org.example.planner.team;

import org.example.planner.membership.MembershipService;
import org.example.planner.team.exception.NoTeamFoundByNameException;
import org.example.planner.team.exception.TeamNameAlreadyExistsException;
import org.example.planner.team.form.CreateTeamForm;
import org.example.planner.team.form.JoinTeamForm;
import org.example.planner.team.form.UpdateTeamForm;
import org.example.planner.team.response.TeamUser;
import org.example.planner.team.response.UserTeam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/teams")
public class TeamController {

    private final MembershipService membershipService;
    private final TeamService teamService;

    public TeamController(MembershipService membershipService, TeamService teamService) {
        this.membershipService = membershipService;
        this.teamService = teamService;
    }

    @GetMapping
    public ModelAndView getUserTeams() {
        List<UserTeam> userTeams = membershipService.getUserTeams();

        ModelAndView result = new ModelAndView("team/teams");
        result.addObject("userTeams", userTeams);
        return result;
    }

    @GetMapping("/{id}/members")
    public ModelAndView getTeamMembers(@PathVariable Integer id) {
        List<TeamUser> teamUsers = membershipService.getTeamUsers(id);
        UserTeam userTeam = membershipService.getUserTeam(id);

        ModelAndView result = new ModelAndView("team/teamMembers");
        result.addObject("teamUsers", teamUsers);
        result.addObject("userTeam", userTeam);
        return result;
    }

    @GetMapping("/create")
    public ModelAndView createTeamForm() {
        ModelAndView result = new ModelAndView("team/createTeam");
        result.addObject("createTeamForm", new CreateTeamForm());
        return result;
    }

    @PostMapping("/create")
    public ModelAndView createTeam(@ModelAttribute CreateTeamForm createTeamForm) {
        try {
            teamService.createTeam(createTeamForm);
        } catch (TeamNameAlreadyExistsException ex) {
            ModelAndView result = new ModelAndView("team/createTeam");
            result.addObject("error", ex.getMessage());
            return result;
        }
        return new ModelAndView("redirect:/teams");
    }

    @GetMapping("/join")
    public ModelAndView joinTeamForm() {
        ModelAndView result = new ModelAndView("team/joinTeam");
        result.addObject("joinTeamForm", new JoinTeamForm());
        return result;
    }

    @PostMapping("/join")
    public ModelAndView joinTeam(@ModelAttribute JoinTeamForm joinTeamForm) {
        try {
            teamService.joinTeam(joinTeamForm);
        } catch (NoTeamFoundByNameException | IllegalArgumentException ex) {
            ModelAndView result = new ModelAndView("team/joinTeam");
            result.addObject("error", ex.getMessage());
            return result;
        }
        return new ModelAndView("redirect:/teams");
    }

    @GetMapping("/update/{id}")
    public ModelAndView updateTeamForm(@PathVariable Integer id) {
        Team team = teamService.getTeamById(id);

        ModelAndView result = new ModelAndView("team/updateTeam");
        result.addObject("updateTeamForm", new UpdateTeamForm(
                team.getId(),
                team.getName(),
                team.getDescription()
        ));
        return result;
    }

    @PostMapping("/update")
    public ModelAndView updateTeam(@ModelAttribute UpdateTeamForm updateTeamForm) {
        try {
            teamService.updateTeam(updateTeamForm);
        } catch (TeamNameAlreadyExistsException ex) {
            ModelAndView result = new ModelAndView("team/updateTeam");
            result.addObject("error", ex.getMessage());
            return result;
        }
        return new ModelAndView("redirect:/teams");
    }

    @PostMapping("/delete/{id}")
    public ModelAndView deleteTeam(@PathVariable Integer id) {
        teamService.deleteTeam(id);
        return new ModelAndView("redirect:/teams");
    }

    @PostMapping("/leave/{id}")
    public ModelAndView leaveTeam(@PathVariable Integer id) {
        membershipService.leaveTeam(id);
        return new ModelAndView("redirect:/teams");
    }

    @PostMapping("/{teamId}/user/{userId}/promote")
    public ModelAndView promoteUser(@PathVariable Integer teamId, @PathVariable Integer userId) {
        membershipService.promoteUser(teamId, userId);
        return new ModelAndView("redirect:/teams/" + teamId + "/members");
    }

    @PostMapping("/{teamId}/user/{userId}/demote")
    public ModelAndView demoteUser(@PathVariable Integer teamId, @PathVariable Integer userId) {
        membershipService.demoteUser(teamId, userId);
        return new ModelAndView("redirect:/teams/" + teamId + "/members");
    }

    @PostMapping("/{teamId}/user/{userId}/kick")
    public ModelAndView kickUser(@PathVariable Integer teamId, @PathVariable Integer userId) {
        membershipService.kickUser(teamId, userId);
        return new ModelAndView("redirect:/teams/" + teamId + "/members");
    }
}
