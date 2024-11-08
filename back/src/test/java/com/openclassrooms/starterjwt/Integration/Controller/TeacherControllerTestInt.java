package com.openclassrooms.starterjwt.Integration.Controller;


import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTestInt {

    @Autowired
    private MockMvc mockmMvc;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    @Tag("GET")
    @DisplayName("Test finding a teacher by valid ID should return the teacher")
    @WithMockUser(username = "user@test.com")
    void testFindById_validId_shouldReturnTeacher() throws Exception {
        // Arrange: create and save a teacher
        Teacher teacher = new Teacher();
        teacher
                .setFirstName("firstName")
                .setLastName("lastName");
        teacherRepository.save(teacher);

        TeacherDto expectedTeacherDto = teacherMapper.toDto(teacher);

        // Act & Assert: perform GET request and verify the response
        mockmMvc.perform(get("/api/teacher/" + teacher.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(expectedTeacherDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(expectedTeacherDto.getLastName())));
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding a teacher by invalid ID should return Bad Request")
    @WithMockUser(username = "user@test.com")
    void testFindById_validId_shouldReturnBadRequest() throws Exception {
        // Act & Assert: perform GET request with an invalid ID and expect Bad Request status
        mockmMvc.perform(get("/api/teacher/invalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Tag("GET")
    @DisplayName("Test finding all teachers should return status OK")
    @WithMockUser(username = "user@test.com")
    void testFindAll_validId_shouldReturnOk() throws Exception {
        // Arrange: create and save multiple teachers
        Teacher teacherUn = new Teacher().setFirstName("firstName").setLastName("lastName");
        Teacher teacherDeux = new Teacher().setFirstName("firstNameDeux").setLastName("lastNameDeux");

        teacherRepository.save(teacherUn);
        teacherRepository.save(teacherDeux);

        // Act & Assert: perform GET request and verify the response contains both teachers
        mockmMvc.perform(get("/api/teacher/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.firstName == '%s' && @.lastName == '%s')]", "firstName", "lastName").exists())
                .andExpect(jsonPath("$[?(@.firstName == '%s' && @.lastName == '%s')]", "firstNameDeux", "lastNameDeux").exists());
    }
}
