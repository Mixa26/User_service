package com.CarRental.UserService.exceptions;

import org.springframework.http.HttpStatus;

public class Forbidden extends CustomException{
    public Forbidden(String message) {
        super(message, ErrorCode.FORBIDDEN_ACCESS, HttpStatus.FORBIDDEN);
    }
}
