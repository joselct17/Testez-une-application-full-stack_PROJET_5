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

@DisplayName("Unit Tests for SignupRequest")
public class SignupRequestTest {

    private SignupRequest signupRequest;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        signupRequest = new SignupRequest();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    };

    @Test
    @DisplayName("Get and Set Email")
    public void testGetAndSetEmail() {
        // Arrange
        String email = "test@email.fr";

        // Act
        signupRequest.setEmail(email);
        String result = signupRequest.getEmail();

        // Assert
        assertEquals(email, result);
    };

    @Test
    @DisplayName("Get and Set First Name")
    public void testGetAndSetFirstName() {
        // Arrange
        String firstName = "firstName";

        // Act
        signupRequest.setFirstName(firstName);
        String result = signupRequest.getFirstName();

        // Assert
        assertEquals(firstName, result);
    };

    @Test
    @DisplayName("Get and Set Last Name")
    public void testGetAndSetLastName() {
        // Arrange
        String lastName = "lastName";

        // Act
        signupRequest.setLastName(lastName);
        String result = signupRequest.getLastName();

        // Assert
        assertEquals(lastName, result);
    };

    @Test
    @DisplayName("Get and Set Password")
    public void testGetAndSetPassword() {
        // Arrange
        String password = "password";

        // Act
        signupRequest.setPassword(password);
        String result = signupRequest.getPassword();

        // Assert
        assertEquals(password, result);
    };

    @Test
    @DisplayName("Initial State")
    public void testInitialState() {
        // Act & Assert
        assertNull(signupRequest.getEmail());
        assertNull(signupRequest.getFirstName());
        assertNull(signupRequest.getLastName());
        assertNull(signupRequest.getPassword());
    };

    @Test
    @DisplayName("Valid SignupRequest")
    public void testValidSignupRequest() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("valid@email.fr");
        signupRequest.setFirstName("firstName");
        signupRequest.setLastName("lastName");
        signupRequest.setPassword("password");

        // Act
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Assert
        assertTrue(violations.isEmpty());
    };

    @Test
    @DisplayName("SignupRequest with Blank Email")
    public void testSignupRequestWithBlankEmail() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("");
        signupRequest.setFirstName("firstName");
        signupRequest.setLastName("lastName");
        signupRequest.setPassword("password");

        // Act
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("SignupRequest with Invalid Email")
    public void testSignupRequestWithInvalidEmail() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("invalid-email");
        signupRequest.setFirstName("firstName");
        signupRequest.setLastName("lastName");
        signupRequest.setPassword("password");

        // Act
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("SignupRequest with Blank First Name")
    public void testSignupRequestWithBlankFirstName() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("valid@email.fr");
        signupRequest.setFirstName("");
        signupRequest.setLastName("lastName");
        signupRequest.setPassword("password");

        // Act
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    };

    @Test
    @DisplayName("SignupRequest with Short First Name")
    public void testSignupRequestWithShortFirstName() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("valid@email.fr");
        signupRequest.setFirstName("fi");
        signupRequest.setLastName("lastName");
        signupRequest.setPassword("password");

        // Act
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("SignupRequest with Blank Last Name")
    public void testSignupRequestWithBlankLastName() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("valid@email.fr");
        signupRequest.setFirstName("firstName");
        signupRequest.setLastName("");
        signupRequest.setPassword("password");

        // Act
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    };

    @Test
    @DisplayName("SignupRequest with Short Last Name")
    public void testSignupRequestWithShortLastName() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("valid@email.fr");
        signupRequest.setFirstName("firstName");
        signupRequest.setLastName("la");
        signupRequest.setPassword("password");

        // Act
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("SignupRequest with Blank Password")
    public void testSignupRequestWithBlankPassword() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("valid@email.fr");
        signupRequest.setFirstName("firstName");
        signupRequest.setLastName("lastName");
        signupRequest.setPassword("");

        // Act
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
    };

    @Test
    @DisplayName("SignupRequest with Short Password")
    public void testSignupRequestWithShortPassword() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("valid@email.fr");
        signupRequest.setFirstName("firstName");
        signupRequest.setLastName("lastName");
        signupRequest.setPassword("123");

        // Act
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };
}
