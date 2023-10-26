package com.ontop.challenge.banksystem.exceptionhandler;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
