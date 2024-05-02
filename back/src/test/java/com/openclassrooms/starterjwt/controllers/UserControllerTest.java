package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testFindById() throws Exception {
        // Prepare
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEmail("yoga@studio.com");
        user.setLastName("Admin");
        user.setFirstName("Admin");
        user.setAdmin(true);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        // Execute and Verify
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("yoga@studio.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Admin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Admin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.admin").value(true));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testFindByIdNonExistentUser() throws Exception {
        // Prepare
        long nonExistentUserId = 9999L; // Non-existent user ID

        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // Execute and Verify
        mockMvc.perform(get("/api/user/{id}", nonExistentUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testFindByIdNumberFormatException() throws Exception {
        // Prepare
        String invalidUserId = "abc"; // Invalid user ID (not a number)

        // Execute and Verify
        mockMvc.perform(get("/api/user/{id}", invalidUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testDelete() throws Exception {
        // Prepare
        long userId = 1L;
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        User user = User.builder()
                .id(Long.valueOf(userId))
                .email("yoga@studio.com")
                .firstName("First name")
                .lastName("Last name")
                .password("test!1234")
                .admin(true)
                .build();
        UserDetails userDetails = UserDetailsImpl.builder().username(user.getEmail()).build();


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Execute and Verify
        mockMvc.perform(delete("/api/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testDeleteNonExistentUser() throws Exception {
        // Prepare
        long nonExistentUserId = 9999L; // Non-existent user ID

        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // Execute and Verify
        mockMvc.perform(delete("/api/user/{id}", nonExistentUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "different@user.com", password = "test!5678", roles = {"USER"})
    public void testDeleteUnauthorizedUser() throws Exception {
        // Prepare
        long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEmail("yoga@studio.com"); // Different user
        user.setLastName("Admin");
        user.setFirstName("Admin");
        user.setAdmin(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Execute and Verify
        mockMvc.perform(delete("/api/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com", password = "test!1234", roles = {"ADMIN"})
    public void testDeleteNumberFormatException() throws Exception {
        // Prepare
        String invalidUserId = "abc"; // Invalid user ID (not a number)

        // Execute and Verify
        mockMvc.perform(delete("/api/user/{id}", invalidUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}