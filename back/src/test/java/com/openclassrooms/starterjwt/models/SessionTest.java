package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Tests for Session")
public class SessionTest {

    private Validator validator;
    private Session session;
    private Teacher teacher;
    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        teacher = Teacher.builder()
                .id(1L)
                .firstName("firstName1")
                .lastName("lastName1")
                .build();

        user1 = User.builder()
                .id(1L)
                .firstName("firstName2")
                .lastName("lastName2")
                .email("user2@email.fr")
                .password("password")
                .admin(false)
                .build();

        user2 = User.builder()
                .id(2L)
                .firstName("firstName3")
                .lastName("lastName3")
                .email("user3@email.fr")
                .password("password")
                .admin(false)
                .build();

        session = Session.builder()
                .id(1L)
                .name("Session Name")
                .date(new Date())
                .description("Session Description")
                .teacher(teacher)
                .users(Arrays.asList(user1, user2))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    };

    @Test
    @DisplayName("Valid Session")
    public void testValidSession() {
        // Act
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        // Assert
        assertTrue(violations.isEmpty());
    };

    @Test
    @DisplayName("Session with Blank Name")
    public void testSessionWithBlankName() {
        // Arrange
        session.setName("");

        // Act
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("Session with Null Date")
    public void testSessionWithNullDate() {
        // Arrange
        session.setDate(null);

        // Act
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("Session with Blank Description")
    public void testSessionWithBlankDescription() {
        // Arrange
        session.setDescription(null);

        // Act
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("Session with Null Teacher")
    public void testSessionWithNullTeacher() {
        // Arrange
        session.setTeacher(null);

        // Act
        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        // Assert
        assertTrue(violations.isEmpty());
    };

    @Test
    @DisplayName("Session with Users")
    public void testSessionWithUsers() {
        // Act
        List<User> users = session.getUsers();

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    };

    @Test
    @DisplayName("Session Timestamps")
    public void testSessionTimestamps() {
        // Act
        LocalDateTime createdAt = session.getCreatedAt();
        LocalDateTime updatedAt = session.getUpdatedAt();

        // Assert
        assertNotNull(createdAt);
        assertNotNull(updatedAt);
    };

    @Test
    @DisplayName("Get and Set Id")
    public void testGetAndSetId() {
        // Arrange
        Long id = 3L;

        // Act
        session.setId(id);
        Long result = session.getId();

        // Assert
        assertEquals(id, result);
    };

    @Test
    @DisplayName("Get and Set Name")
    public void testGetAndSetName() {
        // Arrange
        String name = "New Name";

        // Act
        session.setName(name);
        String result = session.getName();

        // Assert
        assertEquals(name, result);
    };

    @Test
    @DisplayName("Get and Set Description")
    public void testGetAndSetDescription() {
        // Arrange
        String description = "New description";

        // Act
        session.setDescription(description);
        String result = session.getDescription();

        // Assert
        assertEquals(description, result);
    };
}
