package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Teacher Controller Unit Tests")
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        teacher = Teacher.builder()
                .id(1L)
                .lastName("lastName")
                .firstName("firstName")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("lastName");
        teacherDto.setFirstName("firstName");
    };

    @Test
    @DisplayName("Find by ID -> Success")
    void shouldFindById() {
        // Arrange
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        // Act
        ResponseEntity<?> response = teacherController.findById("1");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(teacherDto);
        verify(teacherService, times(1)).findById(1L);
        verify(teacherMapper, times(1)).toDto(teacher);
    };

    @Test
    @DisplayName("Find by ID -> Not Found")
    void shouldReturnNotFoundForFindById() {
        // Arrange
        when(teacherService.findById(anyLong())).thenReturn(null);

        // Act
        ResponseEntity<?> response = teacherController.findById("1");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(teacherService, times(1)).findById(anyLong());
        verify(teacherMapper, never()).toDto(any(Teacher.class));
    };

    @Test
    @DisplayName("Find by ID -> Bad Request")
    void shouldThrowNumberFormatExceptionForFindById() {
        // Act
        ResponseEntity<?> response = teacherController.findById("invalid");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(teacherService, never()).findById(anyLong());
        verify(teacherMapper, never()).toDto(any(Teacher.class));
    };

    @Test
    @DisplayName("Find All -> Success")
    void shouldFindAll() {
        // Arrange
        List<Teacher> teachers = Arrays.asList(teacher);
        List<TeacherDto> dtoTeachers = Collections.singletonList(teacherDto);
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(dtoTeachers);

        // Act
        ResponseEntity<?> response = teacherController.findAll();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dtoTeachers);
        verify(teacherService, times(1)).findAll();
        verify(teacherMapper, times(1)).toDto(teachers);
    };
}
