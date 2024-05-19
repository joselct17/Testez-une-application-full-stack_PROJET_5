package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests for UserDetailsServiceImpl")
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    };

    @Test
    @DisplayName("Load User By Username → Returns UserDetails")
    public void loadUserByUsername_ReturnsUserDetails() {
        // Arrange
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("user@email.fr");
        mockUser.setFirstName("firstName");
        mockUser.setLastName("lastName");
        mockUser.setPassword("password");

        when(userRepository.findByEmail("user@email.fr")).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("user@email.fr");

        // Assert
        assertNotNull(userDetails);
        assertEquals("user@email.fr", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        verify(userRepository, times(1)).findByEmail("user@email.fr");
    };

    @Test
    @DisplayName("Load User By Username → Throws UsernameNotFoundException")
    public void loadUserByUsername_ThrowsUsernameNotFoundException() {
        // Arrange
        String username = "nonexistent@email.fr";
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {userDetailsService.loadUserByUsername(username);});

        verify(userRepository, times(1)).findByEmail(username);
    };
}
