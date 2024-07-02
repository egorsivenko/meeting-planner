package org.example.planner.team.exception;

public class TeamIsFullException extends RuntimeException {

    private static final String MESSAGE = "The team you're trying to join is already full";

    public TeamIsFullException() {
        super(MESSAGE);
    }
}
