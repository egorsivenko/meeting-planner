package org.example.planner.team.exception;

public class UserTeamsLimitExceededException extends RuntimeException {

    private static final String MESSAGE = "You've reached the limit of teams you can be a member of";

    public UserTeamsLimitExceededException() {
        super(MESSAGE);
    }
}
