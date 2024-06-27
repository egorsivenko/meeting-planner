package org.example.planner.invitation;

import org.example.planner.invitation.form.RespondInvitationForm;
import org.example.planner.invitation.mapper.InvitationMapper;
import org.example.planner.invitation.response.InvitationResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvitationService {

    private final InvitationDao invitationDao;
    private final InvitationMapper invitationMapper;

    public InvitationService(InvitationDao invitationDao, InvitationMapper invitationMapper) {
        this.invitationDao = invitationDao;
        this.invitationMapper = invitationMapper;
    }

    public Invitation getInvitationById(Integer meetingId, Integer userId) {
        ICompositeKey compositeKey = new ICompositeKey(meetingId, userId);
        return invitationDao.getById(compositeKey).orElseThrow();
    }

    public List<InvitationResponse> getMeetingInvitations(Integer meetingId) {
        return invitationDao.getByMeetingId(meetingId).stream()
                .map(invitationMapper::toInvitationResponse)
                .toList();
    }

    public void create(Integer meetingId, Integer userId) {
        Invitation invitation = Invitation.builder()
                .compositeKey(new ICompositeKey(meetingId, userId))
                .status(InvitationStatus.ACTIVE)
                .build();

        invitationDao.create(invitation);
    }

    public void update(RespondInvitationForm form) {
        Invitation invitation = Invitation.builder()
                .compositeKey(new ICompositeKey(form.getMeetingId(), form.getUserId()))
                .status(form.getStatus())
                .suggestedTime(form.getSuggestedTime())
                .updateTime(LocalDateTime.now())
                .build();

        invitationDao.update(invitation);
    }
}
