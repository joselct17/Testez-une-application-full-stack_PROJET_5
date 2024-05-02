package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @InjectMocks
    TeacherService teacherService;

    @Mock
    TeacherRepository teacherRepository;


    @Test
    void shouldCallFindAllFromTeacherRepository() {
        // Given
        List<Teacher> teachers = List.of(new Teacher());
        // When
        when(teacherRepository.findAll()).thenReturn(teachers);
        List<Teacher> teachers1 = teacherService.findAll();
        // Then
        verify(teacherRepository, Mockito.times(1)).findAll();
        assertEquals(teachers, teachers1);
    }

    @Test
    void shouldCallFindByIdFromTeacherRepository() {
        // Given
        Long id = 1L;
        Teacher teacher = new Teacher();
        // When
        when(teacherRepository.findById(id)).thenReturn(java.util.Optional.of(teacher));
        Teacher teacher1 = teacherService.findById(id);
        // Then
        verify(teacherRepository, Mockito.times(1)).findById(id);
        assertEquals(teacher, teacher1);

    }
}