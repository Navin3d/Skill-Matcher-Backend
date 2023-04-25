package com.example.skillmatcherbackend.configuration.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.skillmatcherbackend.model.ErrorResponse;
import com.example.skillmatcherbackend.model.document.UserDocument;
import com.example.skillmatcherbackend.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @SuppressWarnings("unchecked")
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {
        try {
            final Map<String, String> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
        } catch (IOException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
        sendAuthenticationSuccessResponse(response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException failed) throws IOException, ServletException {
        sendAuthenticationFailureResponse(response);
    }

    private void sendAuthenticationSuccessResponse(HttpServletResponse response, Authentication authResult) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), jwtService.generateToken((UserDocument) authResult.getPrincipal()));
    }

    private static void sendAuthenticationFailureResponse(final HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(400);
        new ObjectMapper().writeValue(response.getOutputStream(), new ErrorResponse("Bad credentials."));
    }
}