package org.example.planner.meeting;

import org.example.planner.invitation.InvitationService;
import org.example.planner.meeting.form.CreateMeetingForm;
import org.example.planner.meeting.form.UpdateMeetingForm;
import org.example.planner.meeting.mapper.MeetingMapper;
import org.example.planner.meeting.validation.MeetingValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeetingService {

    private final MeetingDao meetingDao;
    private final MeetingValidator meetingValidator;
    private final MeetingMapper meetingMapper;
    private final InvitationService invitationService;

    public MeetingService(MeetingDao meetingDao, MeetingValidator meetingValidator,
                          MeetingMapper meetingMapper, InvitationService invitationService) {
        this.meetingDao = meetingDao;
        this.meetingValidator = meetingValidator;
        this.meetingMapper = meetingMapper;
        this.invitationService = invitationService;
    }

    public List<Meeting> getTeamMeetings(Integer teamId, boolean active) {
        return active ? meetingDao.getActiveTeamMeetings(teamId, LocalDateTime.now())
                : meetingDao.getPastTeamMeetings(teamId, LocalDateTime.now());
    }

    public Meeting getMeetingById(Integer meetingId) {
        return meetingDao.getById(meetingId).orElseThrow();
    }

    public void createMeeting(CreateMeetingForm createMeetingForm) {
        Meeting meeting = meetingMapper.toMeeting(createMeetingForm);

        meetingValidator.validateMeeting(meeting);
        Integer meetingId = meetingDao.create(meeting);

        createMeetingForm.getParticipantsId()
                .forEach(userId -> invitationService.create(meetingId, userId));
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
