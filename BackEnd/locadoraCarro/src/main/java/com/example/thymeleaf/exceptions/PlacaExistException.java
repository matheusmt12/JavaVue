package com.example.thymeleaf.exceptions;

public class PlacaExistException extends RuntimeException{
    public PlacaExistException(String message){
        super(message);
    }
}
