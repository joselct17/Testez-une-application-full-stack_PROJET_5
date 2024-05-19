package com.openclassrooms.starterjwt.security.jwt;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests for AuthEntryPointJwt")
public class AuthEntryPointJwtTest {

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    @Mock
    private HttpServletRequest request;

    @Mock
    private AuthenticationException authException;

    @BeforeEach
    public void setup() {
        when(request.getServletPath()).thenReturn("/test-path");
        when(authException.getMessage()).thenReturn("Unauthorized access attempt");
    }

    @Test
    @DisplayName("Commence -> Unauthorized Access")
    public void commence_ShouldFillResponseWithUnauthorizedErrorDetails() throws Exception {
        // Arrange
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        authEntryPointJwt.commence(request, response, authException);

        // Assert
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("application/json", response.getContentType());

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseBody = mapper.readValue(response.getContentAsString(), Map.class);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, responseBody.get("status"));
        assertEquals("Unauthorized", responseBody.get("error"));
        assertEquals("Unauthorized access attempt", responseBody.get("message"));
        assertEquals("/test-path", responseBody.get("path"));
    };
}