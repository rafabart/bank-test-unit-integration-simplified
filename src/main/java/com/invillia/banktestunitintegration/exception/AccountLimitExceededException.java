package com.invillia.banktestunitintegration.exception;

public class AccountLimitExceededException extends RuntimeException {

    public AccountLimitExceededException(String message) {
        super(message);
    }
}