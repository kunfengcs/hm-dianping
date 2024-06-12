package com.hmdp.config;


public class dianpingException extends RuntimeException {

    private String errMessage;

    public dianpingException() {
        super();
    }

    public dianpingException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

}