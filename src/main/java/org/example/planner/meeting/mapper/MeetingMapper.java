package org.example.planner.meeting.mapper;

import org.example.planner.meeting.Meeting;
import org.example.planner.meeting.MeetingStatus;
import org.example.planner.meeting.form.CreateMeetingForm;
import org.example.planner.meeting.form.UpdateMeetingForm;
import org.example.planner.team.Team;
import org.example.planner.user.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MeetingMapper {

    private final UserService userService;

    public MeetingMapper(UserService userService) {
        this.userService = userService;
    }

    public UpdateMeetingForm toUpdateMeetingForm(Meeting meeting) {
        return UpdateMeetingForm.builder()
                .id(meeting.getId())
                .teamId(meeting.getTeam().getId())
                .subject(meeting.getSubject())
                .startTime(meeting.getStartTime())
                .endTime(meeting.getEndTime())
                .link(meeting.getLink())
                .status(meeting.getStatus())
                .build();
    }

    public Meeting toMeeting(CreateMeetingForm createMeetingForm) {
        return Meeting.builder()
                .organizer(userService.getCurrentUser())
                .team(Team.builder().id(createMeetingForm.getTeamId()).build())
                .subject(createMeetingForm.getSubject().strip())
                .startTime(createMeetingForm.getStartTime())
                .endTime(createMeetingForm.getEndTime())
                .link(createMeetingForm.getLink().strip())
                .status(MeetingStatus.SCHEDULED)
                .creationTime(LocalDateTime.now())
                .build();
    }
}
