package com.example.skillmatcherbackend.model.dto;

import com.example.skillmatcherbackend.model.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO {
    private String name;
    private String email;
    private String password;
    private UserRole role;
}