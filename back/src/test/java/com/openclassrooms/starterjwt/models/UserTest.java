package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Tests for User")
public class UserTest {

    private User user;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        user = User.builder()
                .id(1L)
                .email("test@email.fr")
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .admin(false)
                .build();
    };

    @Test
    @DisplayName("Get and Set Id")
    public void testGetAndSetId() {
        // Arrange
        Long id = 1L;

        // Act
        user.setId(id);
        Long result = user.getId();

        // Assert
        assertEquals(id, result);
    };

    @Test
    @DisplayName("Get and Set Email")
    public void testGetAndSetEmail() {
        // Arrange
        String email = "test@email.fr";

        // Act
        user.setEmail(email);
        String result = user.getEmail();

        // Assert
        assertEquals(email, result);
    };

    @Test
    @DisplayName("Get and Set Last Name")
    public void testGetAndSetLastName() {
        // Arrange
        String lastName = "User";

        // Act
        user.setLastName(lastName);
        String result = user.getLastName();

        // Assert
        assertEquals(lastName, result);
    };

    @Test
    @DisplayName("Get and Set First Name")
    public void testGetAndSetFirstName() {
        // Arrange
        String firstName = "Test";

        // Act
        user.setFirstName(firstName);
        String result = user.getFirstName();

        // Assert
        assertEquals(firstName, result);
    };

    @Test
    @DisplayName("Get and Set Password")
    public void testGetAndSetPassword() {
        // Arrange
        String password = "password";

        // Act
        user.setPassword(password);
        String result = user.getPassword();

        // Assert
        assertEquals(password, result);
    };

    @Test
    @DisplayName("Get and Set Admin")
    public void testGetAndSetAdmin() {
        // Arrange
        boolean admin = true;

        // Act
        user.setAdmin(admin);
        boolean result = user.isAdmin();

        // Assert
        assertEquals(admin, result);
    };

    @Test
    @DisplayName("Get and Set CreatedAt")
    public void testGetAndSetCreatedAt() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now();

        // Act
        user.setCreatedAt(createdAt);
        LocalDateTime result = user.getCreatedAt();

        // Assert
        assertEquals(createdAt, result);
    };

    @Test
    @DisplayName("Get and Set UpdatedAt")
    public void testGetAndSetUpdatedAt() {
        // Arrange
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        user.setUpdatedAt(updatedAt);
        LocalDateTime result = user.getUpdatedAt();

        // Assert
        assertEquals(updatedAt, result);
    };

    @Test
    @DisplayName("Valid User")
    public void testValidUser() {
        // Arrange
        user.setEmail("valid@email.com");
        user.setLastName("LastName");
        user.setFirstName("FirstName");
        user.setPassword("validPassword");
        user.setAdmin(true);

        // Act
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Assert
        assertTrue(violations.isEmpty());
    };

    @Test
    @DisplayName("User with Invalid Email")
    public void testUserWithInvalidEmail() {
        // Arrange
        user.setEmail("invalid-email");

        // Act
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("User with Password Too Long")
    public void testUserWithPasswordTooLong() {
        // Arrange
        String longPassword = "a".repeat(121);
        user.setPassword(longPassword);

        // Act
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("User with First Name Too Long")
    public void testUserWithFirstNameTooLong() {
        // Arrange
        String longFirstName = "a".repeat(21);
        user.setFirstName(longFirstName);

        // Act
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("User with Last Name Too Long")
    public void testUserWithLastNameTooLong() {
        // Arrange
        String longLastName = "a".repeat(21);
        user.setLastName(longLastName);

        // Act
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };
}
