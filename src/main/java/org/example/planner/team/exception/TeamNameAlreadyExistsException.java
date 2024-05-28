package org.example.planner.team.exception;

public class TeamNameAlreadyExistsException extends RuntimeException {

    private static final String MESSAGE = "Team with name '%s' already exists";

    public TeamNameAlreadyExistsException(String teamName) {
        super(String.format(MESSAGE, teamName));
    }
}
