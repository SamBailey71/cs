package com.example.clarityexercise.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

class ApiKeyAuthenticationTest {

    private static final String API_KEY = "Clarity";
    private ApiKeyAuthentication apiKeyAuthentication;

    @BeforeEach
    void setup() {
        final var grantedAuthorities = Collections.singletonList(mock(GrantedAuthority.class));
        apiKeyAuthentication = new ApiKeyAuthentication(API_KEY, grantedAuthorities);
    }

    @Test
    void getCredentials() {
        assertNull(apiKeyAuthentication.getCredentials());
    }

    @Test
    void getPrincipal() {
        assertEquals(API_KEY, apiKeyAuthentication.getPrincipal());
    }
}