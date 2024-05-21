package org.example.planner.meeting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.planner.team.Team;
import org.example.planner.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Meeting {

    private Integer id;
    private User organizer;
    private Team team;
    private String subject;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String link;
    private MeetingStatus status;
    private LocalDateTime creationTime;
    private LocalDateTime updateTime;
}
