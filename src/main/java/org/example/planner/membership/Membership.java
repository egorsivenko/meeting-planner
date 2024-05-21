package org.example.planner.membership;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Membership {

    private MCompositeKey compositeKey;
    private Role role;
    private LocalDate teamJoiningDate;
    private LocalDate roleAssignmentDate;
}
