package org.example.planner.invitation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invitation {

    private ICompositeKey compositeKey;
    private InvitationStatus status;
    private LocalDateTime suggestedTime;
    private LocalDateTime updateTime;
}
