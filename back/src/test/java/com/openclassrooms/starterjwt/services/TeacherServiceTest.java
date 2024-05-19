package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests for TeacherService")
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    private TeacherService teacherService;

    private List<Teacher> teacherList;
    private Teacher teacher1;
    private Teacher teacher2;

    @BeforeEach
    void setUp() {
        teacher1 = new Teacher(1L, "lastName1", "firstName1", LocalDateTime.now(), LocalDateTime.now());
        teacher2 = new Teacher(2L, "lastName2", "firstName2", LocalDateTime.now(), LocalDateTime.now());
        teacherList = Arrays.asList(teacher1, teacher2);

        teacherService = new TeacherService(teacherRepository);
    };

    @Test
    @DisplayName("Find All Teachers → Returns All Teachers")
    public void testFindAllTeachers() {
        // Arrange
        when(teacherRepository.findAll()).thenReturn(teacherList);

        // Act
        List<Teacher> foundTeachers = teacherService.findAll();

        // Assert
        assertEquals(teacherList.size(), foundTeachers.size());
        assertEquals(teacherList, foundTeachers);
        verify(teacherRepository, times(1)).findAll();
    };

    @Test
    @DisplayName("Find All Teachers → Returns No Teachers")
    public void testFindAllTeachers_NoTeachers() {
        // Arrange
        when(teacherRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Teacher> foundTeachers = teacherService.findAll();

        // Assert
        verify(teacherRepository, times(1)).findAll();
        assertTrue(foundTeachers.isEmpty());
    };

    @Test
    @DisplayName("Find Teacher by ID → Returns the Teacher")
    public void testFindTeacherById_ExistingId() {
        // Arrange
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher1));

        // Act
        Teacher result = teacherService.findById(1L);

        // Assert
        verify(teacherRepository, times(1)).findById(1L);
        assertEquals(teacher1, result);
    };

    @Test
    @DisplayName("Find Teacher by ID → Returns Null for Non-Existing ID")
    public void testFindTeacherById_NonExistingId() {
        // Arrange
        when(teacherRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        Teacher result = teacherService.findById(2L);

        // Assert
        verify(teacherRepository, times(1)).findById(2L);
        assertNull(result);
    };
}
