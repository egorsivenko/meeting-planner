package org.example.planner.invitation.mapper;

import org.example.planner.invitation.Invitation;
import org.example.planner.invitation.form.RespondInvitationForm;
import org.example.planner.invitation.response.InvitationResponse;
import org.example.planner.user.User;
import org.example.planner.user.UserService;
import org.springframework.stereotype.Component;

@Component
public class InvitationMapper {

    private final UserService userService;

    public InvitationMapper(UserService userService) {
        this.userService = userService;
    }

    public RespondInvitationForm toRespondInvitationForm(Invitation invitation) {
        return RespondInvitationForm.builder()
                .meetingId(invitation.getCompositeKey().meetingId())
                .userId(invitation.getCompositeKey().userId())
                .status(invitation.getStatus())
                .suggestedTime(invitation.getSuggestedTime())
                .build();
    }

    public InvitationResponse toInvitationResponse(Invitation invitation) {
        Integer userId = invitation.getCompositeKey().userId();
        User user = userService.getById(userId);

        return InvitationResponse.builder()
                .meetingId(invitation.getCompositeKey().meetingId())
                .participantId(user.getId())
                .participantFirstName(user.getFirstName())
                .participantLastName(user.getLastName())
                .status(invitation.getStatus())
                .suggestedTime(invitation.getSuggestedTime())
                .updateTime(invitation.getUpdateTime())
                .build();
    }
}
