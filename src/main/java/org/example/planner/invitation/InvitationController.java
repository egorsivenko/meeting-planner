package org.example.planner.invitation;

import org.example.planner.invitation.form.RespondInvitationForm;
import org.example.planner.invitation.mapper.InvitationMapper;
import org.example.planner.invitation.response.InvitationResponse;
import org.example.planner.meeting.Meeting;
import org.example.planner.meeting.MeetingService;
import org.example.planner.user.User;
import org.example.planner.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/invitations")
public class InvitationController {

    private final InvitationService invitationService;
    private final MeetingService meetingService;
    private final UserService userService;
    private final InvitationMapper invitationMapper;

    public InvitationController(InvitationService invitationService, MeetingService meetingService,
                                UserService userService, InvitationMapper invitationMapper) {
        this.invitationService = invitationService;
        this.meetingService = meetingService;
        this.userService = userService;
        this.invitationMapper = invitationMapper;
    }

    @GetMapping
    public ModelAndView getMeetingInvitations(@RequestParam Integer meetingId) {
        List<InvitationResponse> invitations = invitationService.getMeetingInvitations(meetingId);
        Meeting meeting = meetingService.getMeetingById(meetingId);
        User currentUser = userService.getCurrentUser();

        ModelAndView result = new ModelAndView("invitation/invitations");
        result.addObject("invitations", invitations);
        result.addObject("meeting", meeting);
        result.addObject("currentUser", currentUser);
        result.addObject("currTime", LocalDateTime.now());
        return result;
    }

    @GetMapping("/{meetingId}/{userId}/respond")
    public ModelAndView respondToInvitation(@PathVariable Integer meetingId, @PathVariable Integer userId) {
        Invitation invitation = invitationService.getInvitationById(meetingId, userId);
        RespondInvitationForm form = invitationMapper.toRespondInvitationForm(invitation);
        List<InvitationStatus> statuses = Arrays.stream(InvitationStatus.values())
                .filter(status -> !status.equals(InvitationStatus.ACTIVE))
                .toList();

        ModelAndView result = new ModelAndView("invitation/respondInvitation");
        result.addObject("form", form);
        result.addObject("statuses", statuses);
        return result;
    }

    @PostMapping("/respond")
    public ModelAndView respondToInvitation(@ModelAttribute RespondInvitationForm form) {
        invitationService.update(form);
        return new ModelAndView("redirect:/invitations?meetingId=" + form.getMeetingId());
    }
}
