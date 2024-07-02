package org.example.planner.meeting.exception;

public class ActiveMeetingsLimitExceededException extends RuntimeException {

    private static final String MESSAGE = "The team has reached its active meetings limit";

    public ActiveMeetingsLimitExceededException() {
        super(MESSAGE);
    }
}
