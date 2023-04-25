package com.example.skillmatcherbackend.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.skillmatcherbackend.exception.DefaultException;
import com.example.skillmatcherbackend.model.JWTResponse;
import com.example.skillmatcherbackend.model.document.UserDocument;
import com.example.skillmatcherbackend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiresAt}")
    private Long jwtExpiresAt;
    private final UserRepository userRepository;

    public JWTResponse generateToken(final UserDocument user) {
        final Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());

        final String accessToken = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiresAt * 1000))
                .withClaim("tokenType", "accessToken")
                .sign(algorithm);

        final String refreshToken = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiresAt * 100000))
                .withClaim("tokenType", "refreshToken")
                .sign(algorithm);

        return getJWTResponse(user, accessToken, refreshToken);
    }

    private JWTResponse getJWTResponse(UserDocument user, String accessToken, String refreshToken) {
        final JWTResponse jwtResponse = new JWTResponse();
        jwtResponse.setId(user.getId());
        jwtResponse.setRole(user.getRole());
        jwtResponse.setAccessToken(accessToken);
        jwtResponse.setRefreshToken(refreshToken);
        jwtResponse.setExpiresAt(String.valueOf(jwtExpiresAt));
        return jwtResponse;
    }

    public boolean verifyToken(final String authorizationHeader, final HttpServletResponse response) throws IOException {
        boolean isVerified = false;
        if (doesAuthorizationHeaderContainJWT(authorizationHeader)) {
            try {
                final DecodedJWT decodedJWT = decodeJWT(authorizationHeader.substring(7));
                if (isRefreshToken(decodedJWT)) {
                    throw new DefaultException("Invalid JWT token.");
                }
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(), null, null)
                );
                isVerified = true;
            } catch (Exception e) {
                getJWTVerificationErrorResponse(response, e);
            }
        }
        return isVerified;
    }

    private static boolean isRefreshToken(final DecodedJWT decodedJWT) {
        return decodedJWT.getClaim("tokenType").toString().equals("\"refreshToken\"");
    }

    public JWTResponse generateNewToken(final String refreshToken) throws DefaultException {
        try {
            final DecodedJWT decodedJWT = decodeJWT(refreshToken);
            final UserDocument user = userRepository.findByEmail(decodedJWT.getSubject());
            return generateToken(user);
        } catch (Exception e) {
            throw new DefaultException("JWT token is not valid");
        }
    }

    private static void getJWTVerificationErrorResponse(final HttpServletResponse response, final Exception e) throws IOException {
        Map<String, String> result = new HashMap<>();
        result.put("error", e.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(401);
        new ObjectMapper().writeValue(response.getOutputStream(), result);
    }

    private DecodedJWT decodeJWT(final String token) {
        final Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        final JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    private static boolean doesAuthorizationHeaderContainJWT(final String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }
}