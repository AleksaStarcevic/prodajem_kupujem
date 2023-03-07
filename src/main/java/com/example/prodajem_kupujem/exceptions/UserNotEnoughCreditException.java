package com.example.prodajem_kupujem.exceptions;

public class UserNotEnoughCreditException extends Exception{

    public UserNotEnoughCreditException(String message) {
        super(message);
    }
}
