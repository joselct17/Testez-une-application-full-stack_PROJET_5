package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerTest {

    @MockBean
    private SessionRepository sessionRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testFindById() throws Exception {
        // Prepare
        long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);
        session.setName("Session 1");
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(sessionId);

        when(sessionRepository.findById(sessionId)).thenReturn(java.util.Optional.of(session));

        // Execute and Verify
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", sessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(sessionId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Session 1"));

    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testInvalidUpdatePath() throws Exception {
        // Prepare
        long invalidSessionId = -1L; // Invalid session ID
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(invalidSessionId);
        sessionDto.setName("Invalid Session");

        // Execute and Verify
        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/{id}", invalidSessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sessionDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testSessionNotFound() throws Exception {
        // Prepare
        long nonExistentSessionId = 9999L; // Non-existent session ID

        when(sessionRepository.findById(nonExistentSessionId)).thenReturn(Optional.empty());

        // Execute and Verify
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session/{id}", nonExistentSessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testFindByIdNumberFormatException() throws Exception {
        // Prepare
        String invalidSessionId = "abc"; // Invalid session ID (not a number)

        // Execute and Verify
        mockMvc.perform(get("/api/session/{id}", invalidSessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testFindAll() throws Exception {
        // Prepare
        List<Session> sessions = new ArrayList<>();
        Session session1 = new Session();
        session1.setId(1L);
        sessions.add(session1);
        Session session2 = new Session();
        session2.setId(2L);
        sessions.add(session2);

        when(sessionRepository.findAll()).thenReturn(sessions);

        // Execute and Verify
        mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testCreate() throws Exception {
        // Prepare
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Session 1");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(new ArrayList<>());
        sessionDto.setDescription("Description");


        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(sessionDto);

        Session session = new Session();
        session.setId(1L);
        session.setName("Session 1");
        session.setDescription("Description");
        session.setDate(new Date());
        session.setTeacher(new Teacher());
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Execute and Verify
        mockMvc.perform(MockMvcRequestBuilders.post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Session 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").exists());

    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testUpdate() throws Exception {
        // Prepare
        long sessionId = 1L;
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(sessionId);
        sessionDto.setName("Session 1");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Description");
        sessionDto.setUsers(new ArrayList<>());

        Session session = new Session();
        session.setId(sessionId);
        session.setName("Session 1");
        session.setDescription("Description");

        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Execute and Verify
        mockMvc.perform(MockMvcRequestBuilders.put("/api/session/{id}", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Session 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Description"));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testDelete() throws Exception {
        // Prepare
        long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);

        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // Execute and Verify
        mockMvc.perform(delete("/api/session/{id}", sessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testDeleteNumberFormatException() throws Exception {
        // Prepare
        String invalidSessionId = "abc"; // Invalid session ID (not a number)

        // Execute and Verify
        mockMvc.perform(delete("/api/session/{id}", invalidSessionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testSaveWithNonExistentSession() throws Exception {
        // Prepare
        long nonExistentSessionId = 9999L; // Non-existent session ID

        when(sessionRepository.findById(nonExistentSessionId)).thenReturn(Optional.empty());

        // Execute and Verify
        mockMvc.perform(delete("/api/session/{id}", nonExistentSessionId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testParticipate() throws Exception {
        // Prepare
        long sessionId = 1L;
        long userId = 1L;
        Session session = new Session();
        User user = new User();
        user.setId(3L);
        List<User> users = new ArrayList<>();
        users.add(user);
        session.setUsers(users);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(new Session());

        // Execute and Verify
        mockMvc.perform(MockMvcRequestBuilders.post("/api/session/{id}/participate/{userId}", sessionId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionRepository, times(1)).save(any(Session.class));
        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testNoLongerParticipate() throws Exception {
        // Prepare
        long sessionId = 1L;
        long userId = 1L;
        Session session = new Session();
        User user = new User();
        user.setId(userId);
        List<User> users = new ArrayList<>();
        users.add(user);
        session.setUsers(users);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(new Session());

        // Execute and Verify
        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", sessionId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionRepository, times(1)).save(any(Session.class));
        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testParticipateNumberFormatException() throws Exception {
        // Prepare
        String invalidSessionId = "abc"; // Invalid session ID (not a number)
        String userId = "1"; // Valid user ID
        // Execute and Verify
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", invalidSessionId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testNoLongerParticipateNumberFormatException() throws Exception {
        // Prepare
        String invalidSessionId = "abc"; // Invalid session ID (not a number)
        String userId = "1"; // Valid user ID
        // Execute and Verify
        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", invalidSessionId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // Helper method to convert objects to JSON strings
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}