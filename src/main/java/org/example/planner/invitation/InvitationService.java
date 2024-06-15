package org.example.planner.invitation;

import org.example.planner.invitation.response.InvitationResponse;
import org.example.planner.invitation.form.RespondInvitationForm;
import org.example.planner.user.User;
import org.example.planner.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvitationService {

    private final InvitationDao invitationDao;
    private final UserService userService;

    public InvitationService(InvitationDao invitationDao, UserService userService) {
        this.invitationDao = invitationDao;
        this.userService = userService;
    }

    public Invitation getInvitationById(Integer meetingId, Integer userId) {
        ICompositeKey compositeKey = new ICompositeKey(meetingId, userId);
        return invitationDao.getById(compositeKey).orElseThrow();
    }

    public List<InvitationResponse> getMeetingInvitations(Integer meetingId) {
        return invitationDao.getByMeetingId(meetingId).stream()
                .map(invitation -> {
                    Integer userId = invitation.getCompositeKey().userId();
                    User user = userService.getById(userId);

                    return InvitationResponse.builder()
                            .meetingId(meetingId)
                            .participantId(user.getId())
                            .participantFirstName(user.getFirstName())
                            .participantLastName(user.getLastName())
                            .status(invitation.getStatus())
                            .suggestedTime(invitation.getSuggestedTime())
                            .updateTime(invitation.getUpdateTime())
                            .build();
                })
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
