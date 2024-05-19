package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests for UserService")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .email("test@email.fr")
                .firstName("Test")
                .lastName("User")
                .password("password")
                .admin(false)
                .build();
        userService = new UserService(userRepository);
    };

    @Test
    @DisplayName("Find User by ID → Returns the User")
    public void testFindUserById_UserExists() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.findById(1L);

        // Assert
        verify(userRepository, times(1)).findById(1L);
        assertEquals(mockUser, result);
    };

    @Test
    @DisplayName("Find User by ID → Returns Null for Non-Existing ID")
    public void testFindUserById_UserNotFound() {
        // Arrange
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        User result = userService.findById(2L);

        // Assert
        verify(userRepository, times(1)).findById(2L);
        assertNull(result);
    };

    @Test
    @DisplayName("Delete User by ID")
    public void testDeleteUserById() {
        // Act
        userService.delete(mockUser.getId());

        // Assert
        verify(userRepository, times(1)).deleteById(mockUser.getId());
    };
}
