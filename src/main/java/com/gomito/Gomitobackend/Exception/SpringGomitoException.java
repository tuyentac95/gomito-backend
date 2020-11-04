package com.gomito.Gomitobackend.Exception;

public class SpringGomitoException extends RuntimeException{
    public SpringGomitoException(String exMessage, Exception ex){
        super(exMessage, ex);
    }

    public SpringGomitoException(String exception){
        super(exception);
    }
}
