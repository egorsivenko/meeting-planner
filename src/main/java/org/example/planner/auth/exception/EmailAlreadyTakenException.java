package org.example.planner.auth.exception;

public class EmailAlreadyTakenException extends RuntimeException {

    public static final String MESSAGE = "User with email '%s' already exists";

    public EmailAlreadyTakenException(String email) {
        super(String.format(MESSAGE, email));
    }
}
