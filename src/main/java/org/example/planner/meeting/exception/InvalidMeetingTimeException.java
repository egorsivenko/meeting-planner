package org.example.planner.meeting.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InvalidMeetingTimeException extends RuntimeException {

    private static final String MESSAGE = "Meeting start time cannot be earlier than %s";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public InvalidMeetingTimeException() {
        super(String.format(MESSAGE, formatter.format(LocalDateTime.now())));
    }
}
