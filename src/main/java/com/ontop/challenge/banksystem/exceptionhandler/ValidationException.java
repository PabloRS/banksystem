package com.ontop.challenge.banksystem.exceptionhandler;

public class ValidationException extends RuntimeException{
    public ValidationException(String message){
        super(message);
    }
}
