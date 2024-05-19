package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Tests for LoginRequest")
public class LoginRequestTest {

    private LoginRequest loginRequest;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        loginRequest = new LoginRequest();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    };

    @Test
    @DisplayName("Get and Set Email")
    public void testGetAndSetEmail() {
        // Arrange
        String email = "test@email.fr";

        // Act
        loginRequest.setEmail(email);
        String result = loginRequest.getEmail();

        // Assert
        assertEquals(email, result);
    };

    @Test
    @DisplayName("Get and Set Password")
    public void testGetAndSetPassword() {
        // Arrange
        String password = "testPassword";

        // Act
        loginRequest.setPassword(password);
        String result = loginRequest.getPassword();

        // Assert
        assertEquals(password, result);
    };

    @Test
    @DisplayName("Initial State")
    public void testInitialState() {
        // Act & Assert
        assertNull(loginRequest.getEmail());
        assertNull(loginRequest.getPassword());
    };

    @Test
    @DisplayName("Valid LoginRequest")
    public void testValidLoginRequest() {
        // Arrange
        loginRequest.setEmail("valid@email.com");
        loginRequest.setPassword("validPassword");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertTrue(violations.isEmpty());
    };

    @Test
    @DisplayName("LoginRequest with Blank Email")
    public void testLoginRequestWithBlankEmail() {
        // Arrange
        loginRequest.setEmail("");
        loginRequest.setPassword("validPassword");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("LoginRequest with Blank Password")
    public void testLoginRequestWithBlankPassword() {
        // Arrange
        loginRequest.setEmail("valid@email.com");
        loginRequest.setPassword("");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("LoginRequest with Blank Email and Password")
    public void testLoginRequestWithBlankEmailAndPassword() {
        // Arrange
        loginRequest.setEmail("");
        loginRequest.setPassword("");

        // Act
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    };
}
