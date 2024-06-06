package org.example.planner.meeting;

import org.example.planner.meeting.exception.InvalidLinkException;
import org.example.planner.meeting.exception.InvalidMeetingTimeException;
import org.example.planner.meeting.form.CreateMeetingForm;
import org.example.planner.meeting.form.UpdateMeetingForm;
import org.example.planner.membership.MembershipService;
import org.example.planner.team.response.UserTeam;
import org.example.planner.user.User;
import org.example.planner.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;
    private final MembershipService membershipService;
    private final UserService userService;

    public MeetingController(MeetingService meetingService, MembershipService membershipService, UserService userService) {
        this.meetingService = meetingService;
        this.membershipService = membershipService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getTeamMeetings(@RequestParam Integer teamId, @RequestParam(defaultValue = "true") boolean active) {
        List<Meeting> meetings = meetingService.getTeamMeetings(teamId, active);
        UserTeam userTeam = membershipService.getUserTeam(teamId);
        User currentUser = userService.getCurrentUser();

        ModelAndView result = new ModelAndView("meeting/meetings");
        result.addObject("meetings", meetings);
        result.addObject("userTeam", userTeam);
        result.addObject("currentUser", currentUser);
        result.addObject("currTime", LocalDateTime.now());
        result.addObject("active", active);
        return result;
    }

    @GetMapping("/create")
    public ModelAndView createTeamMeeting(@RequestParam Integer teamId) {
        CreateMeetingForm createMeetingForm = new CreateMeetingForm();
        createMeetingForm.setTeamId(teamId);

        ModelAndView result = new ModelAndView("meeting/createMeeting");
        result.addObject("createMeetingForm", createMeetingForm);
        return result;
    }

    @PostMapping("/create")
    public ModelAndView createTeamMeeting(@ModelAttribute CreateMeetingForm createMeetingForm) {
        try {
            meetingService.createMeeting(createMeetingForm);
        } catch (InvalidLinkException | InvalidMeetingTimeException ex) {
            ModelAndView result = new ModelAndView("meeting/createMeeting");
            result.addObject("error", ex.getMessage());
            return result;
        }
        return new ModelAndView("redirect:/meetings?teamId=" + createMeetingForm.getTeamId());
    }

    @GetMapping("/update")
    public ModelAndView updateMeeting(@RequestParam Integer id) {
        Meeting meeting = meetingService.getMeetingById(id);
        UpdateMeetingForm updateMeetingForm = UpdateMeetingForm.builder()
                .id(id)
                .teamId(meeting.getTeam().getId())
                .subject(meeting.getSubject())
                .startTime(meeting.getStartTime())
                .endTime(meeting.getEndTime())
                .link(meeting.getLink())
                .status(meeting.getStatus())
                .build();

        List<MeetingStatus> statuses = Arrays.asList(MeetingStatus.values());

        ModelAndView result = new ModelAndView("meeting/updateMeeting");
        result.addObject("updateMeetingForm", updateMeetingForm);
        result.addObject("statuses", statuses);
        return result;
    }

    @PostMapping("/update")
    public ModelAndView updateMeeting(@ModelAttribute UpdateMeetingForm updateMeetingForm) {
        try {
            meetingService.updateMeeting(updateMeetingForm);
        } catch (InvalidLinkException | InvalidMeetingTimeException ex) {
            List<MeetingStatus> statuses = Arrays.asList(MeetingStatus.values());

            ModelAndView result = new ModelAndView("meeting/updateMeeting");
            result.addObject("error", ex.getMessage());
            result.addObject("statuses", statuses);
            return result;
        }
        return new ModelAndView("redirect:/meetings?teamId=" + updateMeetingForm.getTeamId());
    }

    @PostMapping("/delete")
    public ModelAndView deleteMeeting(@RequestParam Integer id, @RequestParam Integer teamId) {
        meetingService.deleteMeeting(id);
        return new ModelAndView("redirect:/meetings?teamId=" + teamId);
    }
}
