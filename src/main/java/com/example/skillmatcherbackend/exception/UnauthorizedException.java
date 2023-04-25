package com.example.skillmatcherbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnauthorizedException extends Exception {
    private String message;
}