package org.example.planner.membership;

import org.example.planner.team.Team;
import org.example.planner.team.response.TeamUser;
import org.example.planner.team.response.UserTeam;
import org.example.planner.user.User;
import org.example.planner.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MembershipService {

    private final MembershipDao membershipDao;
    private final UserService userService;

    public MembershipService(MembershipDao membershipDao, UserService userService) {
        this.membershipDao = membershipDao;
        this.userService = userService;
    }

    public UserTeam getUserTeam(Integer teamId) {
        User user = userService.getCurrentUser();
        return membershipDao.getById(new MCompositeKey(teamId, user.getId()))
                .map(UserTeam::new)
                .orElseThrow();
    }

    public List<UserTeam> getUserTeams() {
        User user = userService.getCurrentUser();
        List<Membership> memberships = membershipDao.getUserMemberships(user.getId());
        return memberships.stream()
                .map(UserTeam::new)
                .toList();
    }

    public List<TeamUser> getTeamUsers(Integer teamId) {
        List<Membership> memberships = membershipDao.getTeamMemberships(teamId);
        return memberships.stream()
                .map(TeamUser::new)
                .toList();
    }

    public void addUserToTeam(Team team, Role role) {
        User user = userService.getCurrentUser();
        Membership membership = new Membership(team, user, role, LocalDate.now(), LocalDate.now());
        membershipDao.create(membership);
    }

    public void leaveTeam(Integer teamId) {
        User user = userService.getCurrentUser();
        membershipDao.delete(new MCompositeKey(teamId, user.getId()));
    }

    public void promoteUser(Integer teamId, Integer userId) {
        Membership membership = membershipDao.getById(new MCompositeKey(teamId, userId)).orElseThrow();
        membership.setRole(Role.MANAGER);
        membership.setRoleAssignmentDate(LocalDate.now());
        membershipDao.update(membership);
    }

    public void demoteUser(Integer teamId, Integer userId) {
        Membership membership = membershipDao.getById(new MCompositeKey(teamId, userId)).orElseThrow();
        membership.setRole(Role.MEMBER);
        membership.setRoleAssignmentDate(LocalDate.now());
        membershipDao.update(membership);
    }

    public void kickUser(Integer teamId, Integer userId) {
        membershipDao.delete(new MCompositeKey(teamId, userId));
    }
}
