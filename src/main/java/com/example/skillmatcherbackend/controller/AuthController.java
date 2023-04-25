package com.example.skillmatcherbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.skillmatcherbackend.exception.DefaultException;
import com.example.skillmatcherbackend.model.dto.RefreshTokenDTO;
import com.example.skillmatcherbackend.model.dto.SignInDTO;
import com.example.skillmatcherbackend.model.dto.SignUpDTO;
import com.example.skillmatcherbackend.repository.UserRepository;
import com.example.skillmatcherbackend.service.JwtService;
import com.example.skillmatcherbackend.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {
    private final ProfileService profileService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody SignInDTO signInDTO) {
        return ResponseEntity.ok(jwtService.generateToken(userRepository.findByEmail(signInDTO.getEmail())));
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody final SignUpDTO signUpDTO) throws DefaultException {
        return new ResponseEntity<>(jwtService.generateToken(profileService.saveUser(signUpDTO)), HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody final RefreshTokenDTO refreshTokenDTO) throws DefaultException {
        return new ResponseEntity<>(jwtService.generateNewToken(refreshTokenDTO.getRefreshToken()), HttpStatus.OK);
    }
}
