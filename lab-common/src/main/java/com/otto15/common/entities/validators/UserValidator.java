package com.otto15.common.entities.validators;

public final class UserValidator {

    private static final short MIN_PASSWORD_LENGTH = 4;
    private static final short MAX_LOGIN_LENGTH = 200;

    private UserValidator() {

    }

    public static String getValidatedLogin(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Login can not be empty.");
        }
        if (args.length > 1) {
            throw new IllegalArgumentException("Spaces not allowed in login.");
        }
        if (args[0].length() < 1) {
            throw new IllegalArgumentException("Login must have at least 1 character.");
        }
        if (args[0].length() > MAX_LOGIN_LENGTH) {
            throw new IllegalArgumentException("Login must have at least 1 character.");
        }
        return args[0];
    }

    public static String getValidatedPassword(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Password can not be empty.");
        }
        if (args.length > 1) {
            throw new IllegalArgumentException("Spaces not allowed in password.");
        }
        if (args[0].length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must have at least 4 characters.");
        }
        return args[0];
    }

}
