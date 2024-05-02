package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    void shouldCallDeleteByIdFromUserRepository() {
        // Given
        Long id = 1L;
        // When
        userService.delete(id);
        // Then
        verify(userRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void shouldCallFindByIdFromUserRepository() {
        // Given
        Long id = 1L;
        User user = new User();
        // When
        when(userRepository.findById(id)).thenReturn(java.util.Optional.of(user));
        User user1 = userService.findById(id);
        // Then
        verify(userRepository, Mockito.times(1)).findById(id);
        assertEquals(user, user1);
    }
}