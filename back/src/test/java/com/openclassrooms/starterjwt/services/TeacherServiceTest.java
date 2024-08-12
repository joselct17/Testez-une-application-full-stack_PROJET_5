package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

  @Mock
  TeacherRepository teacherRepository;

  @InjectMocks
  TeacherService classUnderTest;
  
  private Teacher teacher;
  
  @BeforeEach
  public void init() {
    teacher = Teacher.builder().id(1L).lastName("Doe").firstName("John").build();
  }
  
  @Test
  public void findById_shouldReturnTeacher() {
    when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));

    Teacher existingTeacher = classUnderTest.findById(teacher.getId());

    verify(teacherRepository, times(1)).findById(teacher.getId());
    assertThat(existingTeacher).isNotNull();
  }
  
  @Test
  public void findAll_shouldReturnTeachers() {
    
    Teacher teacher2 = Teacher.builder().id((long) 2).lastName("Doe").firstName("Jane").build();
    
    when(teacherRepository.findAll()).thenReturn(List.of(teacher, teacher2));

    List<Teacher> existingTeachers = classUnderTest.findAll();

    verify(teacherRepository, times(1)).findAll();
    assertThat(existingTeachers).isNotNull();
    assertThat(existingTeachers.size()).isEqualTo(2);
  }
  
}
