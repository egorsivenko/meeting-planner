package org.example.planner.meeting;

import org.example.planner.meeting.form.CreateMeetingForm;
import org.example.planner.meeting.form.UpdateMeetingForm;
import org.example.planner.meeting.validation.MeetingValidator;
import org.example.planner.team.Team;
import org.example.planner.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeetingService {

    private final MeetingDao meetingDao;
    private final UserService userService;
    private final MeetingValidator meetingValidator;

    public MeetingService(MeetingDao meetingDao, UserService userService, MeetingValidator meetingValidator) {
        this.meetingDao = meetingDao;
        this.userService = userService;
        this.meetingValidator = meetingValidator;
    }

    public List<Meeting> getTeamMeetings(Integer teamId, boolean active) {
        return active ? meetingDao.getActiveTeamMeetings(teamId, LocalDateTime.now())
                : meetingDao.getPastTeamMeetings(teamId, LocalDateTime.now());
    }

    public Meeting getMeetingById(Integer meetingId) {
        return meetingDao.getById(meetingId).orElseThrow();
    }

    public void createMeeting(CreateMeetingForm createMeetingForm) {
        Meeting meeting = Meeting.builder()
                .organizer(userService.getCurrentUser())
                .team(Team.builder().id(createMeetingForm.getTeamId()).build())
                .subject(createMeetingForm.getSubject().strip())
                .startTime(createMeetingForm.getStartTime())
                .endTime(createMeetingForm.getEndTime())
                .link(createMeetingForm.getLink().strip())
                .status(MeetingStatus.SCHEDULED)
                .creationTime(LocalDateTime.now())
                .build();

        meetingValidator.validateMeeting(meeting);
        meetingDao.create(meeting);
    }

    public void updateMeeting(UpdateMeetingForm updateMeetingForm) {
        Meeting meeting = meetingDao.getById(updateMeetingForm.getId()).orElseThrow();
        meeting.setSubject(updateMeetingForm.getSubject());
        meeting.setStartTime(updateMeetingForm.getStartTime());
        meeting.setEndTime(updateMeetingForm.getEndTime());
        meeting.setLink(updateMeetingForm.getLink());
        meeting.setStatus(updateMeetingForm.getStatus());
        meeting.setUpdateTime(LocalDateTime.now());

        meetingValidator.validateMeeting(meeting);
        meetingDao.update(meeting);
    }

    public void deleteMeeting(Integer meetingId) {
        meetingDao.delete(meetingId);
    }
}
