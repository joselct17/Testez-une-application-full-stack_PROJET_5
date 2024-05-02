package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherRepository teacherRepository;

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testFindById() throws Exception {
        // Prepare
        long teacherId = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);

        when(teacherRepository.findById(teacherId)).thenReturn(java.util.Optional.of(teacher));

        // Execute and Verify
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/{id}", teacherId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(teacherId));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testFindByIdNonExistentTeacher() throws Exception {
        // Prepare
        long nonExistentTeacherId = 9999L; // Non-existent user ID

        when(teacherRepository.findById(nonExistentTeacherId)).thenReturn(Optional.empty());

        // Execute and Verify
        mockMvc.perform(get("/api/teacher/{id}", nonExistentTeacherId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testFindByIdNumberFormatException() throws Exception {
        // Prepare
        String invalidTeacherId = "abc"; // Invalid teacher ID (not a number)

        // Execute and Verify
        mockMvc.perform(get("/api/teacher/{id}", invalidTeacherId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testFindAll() throws Exception {
        // Prepare
        List<Teacher> teachers = new ArrayList<>();
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teachers.add(teacher1);
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teachers.add(teacher2);

        when(teacherRepository.findAll()).thenReturn(teachers);

        // Execute and Verify
        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));
    }
}