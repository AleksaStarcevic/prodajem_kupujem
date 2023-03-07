package com.example.prodajem_kupujem.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(AdvertisementNotFoundException.class)
    public ResponseEntity<String> handleAdvertisementNotFoundException(AdvertisementNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(AdvertisementPromotionNotFoundException.class)
    public ResponseEntity<String> handleAdvertisementPromotionNotFoundException(AdvertisementPromotionNotFoundException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(UserNotEnoughCreditException.class)
    public ResponseEntity<String> handleUserNotEnoughCreditException(UserNotEnoughCreditException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

}
