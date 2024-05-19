package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.springframework.test.util.ReflectionTestUtils;


@SpringBootTest
@DisplayName("Unit Tests for JwtUtils")
public class JwtUtilsTest {

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "JwtSecretTest");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000); // 1 hour

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("user@email.fr")
                .password("password")
                .build();

        when(authentication.getPrincipal()).thenReturn(userDetails);
    };

    @Test
    @DisplayName("Generate JWT Token")
    public void generateJwtToken_ReturnsValidToken() {
        // Act
        String token = jwtUtils.generateJwtToken(authentication);

        // Assert
        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
    };

    @Test
    @DisplayName("Get Username From JWT Token -> Returns the username")
    public void getUsernameFromJwtToken_ReturnsCorrectUsername() {
        // Arrange
        String token = jwtUtils.generateJwtToken(authentication);

        // Act
        String username = jwtUtils.getUserNameFromJwtToken(token);

        // Assert
        assertEquals("user@email.fr", username);
    };

    @Test
    @DisplayName("Validate JWT Token -> Returns True")
    public void validateJwtToken_ValidToken_ReturnsTrue() {
        // Arrange
        String token = jwtUtils.generateJwtToken(authentication);

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertTrue(isValid);
    };

    @Test
    @DisplayName("Validate JWT Token -> Returns False")
    public void validateJwtToken_InvalidToken_ReturnsFalse() {
        // Arrange
        String token = "invalid.token.here";

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertFalse(isValid);
    };
}
