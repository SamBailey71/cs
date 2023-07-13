package com.example.clarityexercise.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class AuthenticationFilterTest {

    private static final String HEADER_NAME = "X-API-KEY";
    @InjectMocks
    private AuthenticationFilter authenticationFilter;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private HttpServletRequest servletRequest;
    @Mock
    private HttpServletResponse servletResponse;
    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        authenticationFilter = new AuthenticationFilter();
    }

    @Test
    void doFilter() throws ServletException, IOException {
        when(servletRequest.getHeader(HEADER_NAME)).thenReturn("Clarity");
        authenticationFilter.doFilter(servletRequest, servletResponse, filterChain);
        assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
    }

    @Test
    void doFilterWrongAPIKey() throws ServletException, IOException {
        when(servletRequest.getHeader(HEADER_NAME)).thenReturn("AnotherKey");
        when(servletResponse.getWriter()).thenReturn(printWriter);
        authenticationFilter.doFilter(servletRequest, servletResponse, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}