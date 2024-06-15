package org.example.planner.invitation.response;

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
public class InvitationResponse {

    private Integer meetingId;
    private Integer participantId;
    private String participantFirstName;
    private String participantLastName;
    private InvitationStatus status;
    private LocalDateTime suggestedTime;
    private LocalDateTime updateTime;
}
