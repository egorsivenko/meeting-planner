package org.example.planner.meeting.validation;

import org.example.planner.meeting.Meeting;
import org.example.planner.meeting.exception.InvalidMeetingTimeException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MeetingValidator {

    private final LinkValidator linkValidator;

    public MeetingValidator(LinkValidator linkValidator) {
        this.linkValidator = linkValidator;
    }

    public void validateMeeting(Meeting meeting) {
        validateMeetingTime(meeting);
        linkValidator.validateLink(meeting.getLink());
    }

    private void validateMeetingTime(Meeting meeting) {
        if (meeting.getStartTime().isBefore(LocalDateTime.now())) {
            throw new InvalidMeetingTimeException();
        }
    }
}
