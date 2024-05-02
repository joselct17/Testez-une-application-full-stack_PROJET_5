package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(loginRequest);
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.admin").exists());
    }

    @Test
    void testRegister() throws Exception {
        Random random = new Random();
        int randomNumber = random.nextInt(10); // Generates a random number between 0 (inclusive) and 10 (exclusive)

        // Generate a unique UUID
        UUID uuid = UUID.randomUUID();
        String uniqueNumber = uuid.toString().substring(0, 8);
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"lastName\": \"toto\",\n" +
                                "    \"firstName\": \"toto\",\n" +
                                "    \"email\": \"toto" + uniqueNumber + "@toto.com\",\n" +
                                "    \"password\": \"test!1234\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }

    /*@Test
    void testRegister() throws Exception {
        // Given
        String lastName = "toto";
        String firstName = "toto";
        String email = "toto5@toto.com";
        String password = "test!123";

        // When
        when(userRepository.existsByEmail(email)).thenReturn(false);
        userRepository.save(new User(email, lastName, firstName, password, false));

        // Then
        verify(userRepository, Mockito.times(1)).save(any(User.class));

    }*/

    @Test
    void testRegisterWithExistingEmail() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"lastName\": \"toto\",\n" +
                                "    \"firstName\": \"toto\",\n" +
                                "    \"email\": \"toto3@toto.com\",\n" +
                                "    \"password\": \"test!1234\"\n" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
    }

}