package org.example.planner.invitation.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.planner.invitation.InvitationStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespondInvitationForm {

    private Integer meetingId;
    private Integer userId;
    private InvitationStatus status;
    private LocalDateTime suggestedTime;
}
