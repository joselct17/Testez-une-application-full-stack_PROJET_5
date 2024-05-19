package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Tests for Teacher")
public class TeacherTest {

    private Validator validator;
    private Teacher teacher;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        teacher = Teacher.builder()
                .id(1L)
                .lastName("lastName")
                .firstName("firstName")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    };

    @Test
    @DisplayName("Valid Teacher")
    public void testValidTeacher() {
        // Act
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        // Assert
        assertTrue(violations.isEmpty());
    };

    @Test
    @DisplayName("Teacher with Blank Last Name")
    public void testTeacherWithBlankLastName() {
        // Arrange
        teacher.setLastName("");

        // Act
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("Teacher with Blank First Name")
    public void testTeacherWithBlankFirstName() {
        // Arrange
        teacher.setFirstName("");

        // Act
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("Teacher with Long Last Name")
    public void testTeacherWithLongLastName() {
        // Arrange
        teacher.setLastName("a".repeat(121));

        // Act
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("Teacher with Long First Name")
    public void testTeacherWithLongFirstName() {
        // Arrange
        teacher.setFirstName("a".repeat(121));

        // Act
        Set<ConstraintViolation<Teacher>> violations = validator.validate(teacher);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    };

    @Test
    @DisplayName("Teacher Timestamps")
    public void testTeacherTimestamps() {
        // Act
        LocalDateTime createdAt = teacher.getCreatedAt();
        LocalDateTime updatedAt = teacher.getUpdatedAt();

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
        teacher.setId(id);
        Long result = teacher.getId();

        // Assert
        assertEquals(id, result);
    };

    @Test
    @DisplayName("Get and Set Last Name")
    public void testGetAndSetLastName() {
        // Arrange
        String lastName = "NewLastName";

        // Act
        teacher.setLastName(lastName);
        String result = teacher.getLastName();

        // Assert
        assertEquals(lastName, result);
    };

    @Test
    @DisplayName("Get and Set First Name")
    public void testGetAndSetFirstName() {
        // Arrange
        String firstName = "NewFirstName";

        // Act
        teacher.setFirstName(firstName);
        String result = teacher.getFirstName();

        // Assert
        assertEquals(firstName, result);
    };
}
