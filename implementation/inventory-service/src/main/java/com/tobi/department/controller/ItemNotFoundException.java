package com.tobi.department.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom ItemNotFoundException
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "item not found")
public class ItemNotFoundException extends RuntimeException {

    private int errorCode;
    private String errorMessage;

    public ItemNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public ItemNotFoundException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public ItemNotFoundException(String msg) {
        super(msg);
    }

    public ItemNotFoundException(String message, int errorCode) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = message;
    }


    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
