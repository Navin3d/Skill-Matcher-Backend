package com.example.skillmatcherbackend.service;

import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.skillmatcherbackend.model.document.UserDocument;
import com.example.skillmatcherbackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSession {

    private final UserRepository userRepo;

    public String getUsername() {
        return getPrincipal()
                .map(user -> user.getPrincipal().toString())
                .orElse(null);
    }

    public UserDocument getUser() {
        return userRepo.findByEmail(getUsername());
    }

    public Optional<UsernamePasswordAuthenticationToken> getPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication();
        return principal instanceof UsernamePasswordAuthenticationToken ? Optional.of((UsernamePasswordAuthenticationToken) principal) : Optional.empty();
    }

}
