package com.cd.tech.report.exception;

/**
 * Created by zc on 2017/4/11.
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message, Throwable t) {
        super(message, t);
    }

    public ServiceException(Throwable t) {
        super(t);
    }

    public ServiceException(String message) {
        super(message);
    }
}
