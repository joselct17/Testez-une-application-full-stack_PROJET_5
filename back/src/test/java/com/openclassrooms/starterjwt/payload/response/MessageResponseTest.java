package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Unit Tests for MessageResponse")
public class MessageResponseTest {

    private MessageResponse messageResponse;

    @BeforeEach
    public void setUp() {
        messageResponse = new MessageResponse("Initial message");
    };

    @Test
    @DisplayName("Constructor and Getter")
    public void testConstructorAndGetter() {
        // Assert
        assertNotNull(messageResponse);
        assertEquals("Initial message", messageResponse.getMessage());
    };

    @Test
    @DisplayName("Setter")
    public void testSetter() {
        // Arrange
        messageResponse.setMessage("New message");

        // Assert
        assertEquals("New message", messageResponse.getMessage());
    };
}
