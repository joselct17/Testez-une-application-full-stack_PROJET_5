package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Unit Tests for JwtResponse")
public class JwtResponseTest {

    private JwtResponse jwtResponse;

    @BeforeEach
    public void setUp() {
        jwtResponse = new JwtResponse("testToken", 1L, "testUsername", "testFirstName", "testLastName", true);
    };

    @Test
    @DisplayName("Constructor and Getters")
    public void testConstructorAndGetters() {
        // Assert
        assertNotNull(jwtResponse);
        assertEquals("testToken", jwtResponse.getToken());
        assertEquals("Bearer", jwtResponse.getType());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("testUsername", jwtResponse.getUsername());
        assertEquals("testFirstName", jwtResponse.getFirstName());
        assertEquals("testLastName", jwtResponse.getLastName());
        assertEquals(true, jwtResponse.getAdmin());
    };

    @Test
    @DisplayName("Setters")
    public void testSetters() {
        // Arrange
        jwtResponse.setToken("newToken");
        jwtResponse.setType("newType");
        jwtResponse.setId(2L);
        jwtResponse.setUsername("newUsername");
        jwtResponse.setFirstName("newFirstName");
        jwtResponse.setLastName("newLastName");
        jwtResponse.setAdmin(false);

        // Assert
        assertEquals("newToken", jwtResponse.getToken());
        assertEquals("newType", jwtResponse.getType());
        assertEquals(2L, jwtResponse.getId());
        assertEquals("newUsername", jwtResponse.getUsername());
        assertEquals("newFirstName", jwtResponse.getFirstName());
        assertEquals("newLastName", jwtResponse.getLastName());
        assertEquals(false, jwtResponse.getAdmin());
    };
}
