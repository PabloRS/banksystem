package com.ontop.challenge.banksystem.exceptionhandler;

public class TransactionNotValidException extends RuntimeException{
    public TransactionNotValidException(String message) {
        super(message);
    }
}
