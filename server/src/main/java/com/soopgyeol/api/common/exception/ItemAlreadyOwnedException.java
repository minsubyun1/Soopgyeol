package com.soopgyeol.api.common.exception;

public class ItemAlreadyOwnedException extends RuntimeException {
    public ItemAlreadyOwnedException(String message) {
        super(message);
    }
}

