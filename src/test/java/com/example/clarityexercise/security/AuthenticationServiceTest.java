package com.example.clarityexercise.security;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    private static final String HEADER_NAME = "X-API-KEY";
    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCorrectAuthentication() {
        when(httpServletRequest.getHeader(HEADER_NAME)).thenReturn("Clarity");
        final var actual = AuthenticationService.getAuthentication(httpServletRequest);
        assertTrue(actual.isAuthenticated());
    }

    @Test
    void getWrongApiKey() {
        when(httpServletRequest.getHeader(HEADER_NAME)).thenReturn("AnotherKey");
        assertThrows(BadCredentialsException.class, () -> AuthenticationService.getAuthentication(httpServletRequest));
    }
}