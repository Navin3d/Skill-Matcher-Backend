package com.example.skillmatcherbackend.configuration.filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.example.skillmatcherbackend.service.JwtService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private static final List<String> IGNORED_URL_PATTERNS = new ArrayList<>(Arrays.asList("/swagger-ui/", "/swagger-ui.html", "/swagger-uui.html", "/webjars/springfox-swagger-ui/springfox.css", "/webjars/springfox-swagger-ui/swagger-ui-bundle.js", "/webjars/springfox-swagger-ui/swagger-ui.css", "/webjars/springfox-swagger-ui/swagger-ui-standalone-preset.js", "/webjars/springfox-swagger-ui/springfox.js", "/swagger-resources/configuration/ui", "/webjars/springfox-swagger-ui/favicon-32x32.png", "/swagger-resources/configuration/security", "/swagger-resources", "/v2/api-docs", "/webjars/springfox-swagger-ui/fonts/titillium-web-v6-latin-700.woff2", "/webjars/springfox-swagger-ui/fonts/open-sans-v15-latin-regular.woff2", "/webjars/springfox-swagger-ui/fonts/open-sans-v15-latin-700.woff2", "/webjars/springfox-swagger-ui/favicon-16x16.png", "/swagger-resources/**", "/swagger-ui.html", "/swagger-ui/**", "/v2/api-docs", "/webjars/**", "/swagger-ui/springfox.css", "/swagger-ui/swagger-ui.css", "/swagger-ui/swagger-ui-bundle.js", "/swagger-ui/swagger-ui-standalone-preset.js", "/swagger-ui/springfox.js",
            "/swagger-ui/favicon-32x32.png", "/swagger-ui/favicon-16x16.png", "/api/auth/signIn", "/api/auth/signUp", "/api/auth/refreshToken"));

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        if (jwtService.verifyToken(request.getHeader(AUTHORIZATION), response)) {
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        final String path = request.getRequestURI();
        return IGNORED_URL_PATTERNS.contains(path);
    }
}