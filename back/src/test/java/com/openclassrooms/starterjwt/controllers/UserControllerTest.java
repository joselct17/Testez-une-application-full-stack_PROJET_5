package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Controller Unit Tests")
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@email.fr")
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .admin(false)
                .build();

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@email.fr");
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
    };

    @Test
    @DisplayName("Find by ID -> Success")
    void shouldFindById() {
        // Arrange
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Act
        ResponseEntity<?> response = userController.findById("1");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userDto);
        verify(userService, times(1)).findById(1L);
        verify(userMapper, times(1)).toDto(user);
    };

    @Test
    @DisplayName("Find by ID -> Not Found")
    void shouldReturnNotFoundForFindById() {
        // Arrange
        when(userService.findById(anyLong())).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.findById("1");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userService, times(1)).findById(anyLong());
        verify(userMapper, never()).toDto(any(User.class));
    };

    @Test
    @DisplayName("Find by ID -> Bad Request")
    void shouldThrowNumberFormatExceptionForFindById() {
        // Act
        ResponseEntity<?> response = userController.findById("invalid");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(userService, never()).findById(anyLong());
        verify(userMapper, never()).toDto(any(User.class));
    };

    @Test
    @DisplayName("Delete -> Success")
    void shouldDeleteWithAuthorizedUser() {
        // Arrange
        when(userService.findById(1L)).thenReturn(user);

        // Mock the SecurityContextHolder
        UserDetails userDetails = UserDetailsImpl.builder().username(user.getEmail()).build();
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        ResponseEntity<?> response = userController.save("1");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService, times(1)).findById(1L);
        verify(userService, times(1)).delete(1L);
    };

    @Test
    @DisplayName("Delete -> Not Found")
    void shouldReturnNotFoundForDelete() {
        // Arrange
        when(userService.findById(anyLong())).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.save("1");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userService, times(1)).findById(anyLong());
        verify(userService, never()).delete(anyLong());
    };

    @Test
    @DisplayName("Delete -> Bad Request")
    void shouldThrowNumberFormatExceptionForDelete() {
        // Act
        ResponseEntity<?> response = userController.save("invalid");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(userService, never()).findById(anyLong());
        verify(userService, never()).delete(anyLong());
    };

    @Test
    @DisplayName("Delete -> Unauthorized")
    void shouldReturnUnauthorizedForDelete() {
        // Arrange
        when(userService.findById(1L)).thenReturn(user);

        // Mock the SecurityContextHolder
        UserDetails userDetails = UserDetailsImpl.builder().username("another@example.com").build();
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        ResponseEntity<?> response = userController.save("1");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        verify(userService, times(1)).findById(1L);
        verify(userService, never()).delete(1L);
    };
}
