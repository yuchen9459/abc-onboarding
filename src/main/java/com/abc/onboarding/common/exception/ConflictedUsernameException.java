package com.abc.onboarding.common.exception;

public class ConflictedUsernameException extends RuntimeException{

    String errorCode;

    public ConflictedUsernameException(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
