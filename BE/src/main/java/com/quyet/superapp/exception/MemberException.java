package com.quyet.superapp.exception;

public class MemberException extends RuntimeException{
    private final String errorCode;

    public MemberException(String errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
