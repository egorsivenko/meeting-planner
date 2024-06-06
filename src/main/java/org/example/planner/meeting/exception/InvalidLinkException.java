package org.example.planner.meeting.exception;

public class InvalidLinkException extends RuntimeException {

    private static final String MESSAGE = "Provided link is invalid or inactive";

    public InvalidLinkException() {
        super(MESSAGE);
    }
}
