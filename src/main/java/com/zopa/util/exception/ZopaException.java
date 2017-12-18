package com.zopa.util.exception;

public class ZopaException extends Exception {

    protected String beautyMessage;

    public ZopaException(String logMessage) {
        super(logMessage);
        this.beautyMessage = logMessage;
    }

    public String getBeautyMessage() {
        return beautyMessage;
    }
}
