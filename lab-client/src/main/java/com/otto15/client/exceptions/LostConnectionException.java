package com.otto15.client.exceptions;

public class LostConnectionException extends RuntimeException {

    public LostConnectionException(String message) {
        super(message);
    }

}
