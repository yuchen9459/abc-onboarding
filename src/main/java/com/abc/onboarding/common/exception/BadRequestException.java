package com.abc.onboarding.common.exception;

public class BadRequestException extends RuntimeException {

    String errorCode;

    public BadRequestException(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
