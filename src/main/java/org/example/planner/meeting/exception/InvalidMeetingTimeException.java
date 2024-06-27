package org.example.planner.meeting.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InvalidMeetingTimeException extends RuntimeException {

    private static final String MESSAGE = "Meeting start time cannot be earlier than %s";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public InvalidMeetingTimeException(LocalDateTime dateTime) {
        super(String.format(MESSAGE, FORMATTER.format(dateTime)));
    }
}
