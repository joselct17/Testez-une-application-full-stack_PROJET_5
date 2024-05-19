package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Unit Tests for NotFoundException")
public class NotFoundExceptionTest {

    @Test
    @DisplayName("NotFoundException should have HttpStatus.NOT_FOUND")
    public void testNotFoundExceptionHttpStatus() {
        // Act
        NotFoundException exception = new NotFoundException();

        // Assert
        ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);
        assertEquals(HttpStatus.NOT_FOUND, responseStatus.value());
    };

    @Test
    @DisplayName("NotFoundException should be thrown")
    public void testNotFoundExceptionThrown() {
        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException();
        });
    };
}
