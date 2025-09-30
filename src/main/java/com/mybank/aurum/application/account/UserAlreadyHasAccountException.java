package com.mybank.aurum.application.account;

public class UserAlreadyHasAccountException extends RuntimeException {
    public UserAlreadyHasAccountException(String message) {
        super(message);
    }
}
