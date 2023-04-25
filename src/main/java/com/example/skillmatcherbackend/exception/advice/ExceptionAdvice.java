package com.example.skillmatcherbackend.exception.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.skillmatcherbackend.exception.DefaultException;
import com.example.skillmatcherbackend.exception.UnauthorizedException;
import com.example.skillmatcherbackend.model.ErrorResponse;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(DefaultException.class)
    @ResponseBody
    public ResponseEntity<?> handleDefaultException(final DefaultException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public ResponseEntity<?> handleAuthorizationException(final UnauthorizedException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}