package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests for TeacherMapper")
public class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    public void setUp() {
        teacherMapper = Mappers.getMapper(TeacherMapper.class);

        teacher = Teacher.builder()
                .id(1L)
                .lastName("lastName")
                .firstName("firstName")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        teacherDto = new TeacherDto(
                1L,
                "lastName",
                "firstName",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    };

    @Test
    @DisplayName("toEntity should map TeacherDto to Teacher correctly")
    public void testToEntity() {
        // Act
        Teacher result = teacherMapper.toEntity(teacherDto);

        // Assert
        assertNotNull(result);
        assertEquals(teacherDto.getId(), result.getId());
        assertEquals(teacherDto.getLastName(), result.getLastName());
        assertEquals(teacherDto.getFirstName(), result.getFirstName());
        assertEquals(teacherDto.getCreatedAt(), result.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), result.getUpdatedAt());
    };

    @Test
    @DisplayName("toDto should map Teacher to TeacherDto correctly")
    public void testToDto() {
        // Act
        TeacherDto result = teacherMapper.toDto(teacher);

        // Assert
        assertNotNull(result);
        assertEquals(teacher.getId(), result.getId());
        assertEquals(teacher.getLastName(), result.getLastName());
        assertEquals(teacher.getFirstName(), result.getFirstName());
        assertEquals(teacher.getCreatedAt(), result.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), result.getUpdatedAt());
    };

    @Test
    @DisplayName("toEntityList should map list of TeacherDto to list of Teacher correctly")
    public void testToEntityList() {
        // Arrange
        List<TeacherDto> teacherDtoList = Collections.singletonList(teacherDto);

        // Act
        List<Teacher> result = teacherMapper.toEntity(teacherDtoList);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(teacherDto.getId(), result.get(0).getId());
        assertEquals(teacherDto.getLastName(), result.get(0).getLastName());
        assertEquals(teacherDto.getFirstName(), result.get(0).getFirstName());
        assertEquals(teacherDto.getCreatedAt(), result.get(0).getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), result.get(0).getUpdatedAt());
    };

    @Test
    @DisplayName("toDtoList should map list of Teacher to list of TeacherDto correctly")
    public void testToDtoList() {
        // Arrange
        List<Teacher> teacherList = Collections.singletonList(teacher);

        // Act
        List<TeacherDto> result = teacherMapper.toDto(teacherList);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(teacher.getId(), result.get(0).getId());
        assertEquals(teacher.getLastName(), result.get(0).getLastName());
        assertEquals(teacher.getFirstName(), result.get(0).getFirstName());
        assertEquals(teacher.getCreatedAt(), result.get(0).getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), result.get(0).getUpdatedAt());
    };
}
