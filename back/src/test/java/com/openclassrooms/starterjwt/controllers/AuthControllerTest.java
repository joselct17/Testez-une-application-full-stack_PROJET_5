package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests for AuthController")
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    private LoginRequest loginRequest;
    private SignupRequest signUpRequest;

    @BeforeEach
    public void setup() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        signUpRequest = new SignupRequest();
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setFirstName("FirstName");
        signUpRequest.setLastName("LastName");
        signUpRequest.setPassword("password");

    };

    @Test
    @DisplayName("Authenticate User -> Success")
    public void authenticateUser_returnJwtResponse() {
        // Arrange
        User user = new User("test@example.com", "LastName", "FirstName", "password", true);
        user.setId(1L);

        UserDetailsImpl userDetails = new UserDetailsImpl(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.isAdmin(), user.getPassword());

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(jwtUtils.generateJwtToken(auth)).thenReturn("jwtToken");

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertNotNull(jwtResponse);
        assertEquals("jwtToken", jwtResponse.getToken());
        assertEquals(user.getId(), jwtResponse.getId());
        assertEquals(user.getEmail(), jwtResponse.getUsername());
        assertEquals(user.getFirstName(), jwtResponse.getFirstName());
        assertEquals(user.getLastName(), jwtResponse.getLastName());
        assertTrue(jwtResponse.getAdmin());
    };


    @Test
    @DisplayName("Authenticate User -> Failure")
    public void authenticateUser_withInvalidCredentials_ShouldThrowException() {
        // Arrange
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            authController.authenticateUser(loginRequest);
        });
        assertThat(exception).isInstanceOf(BadCredentialsException.class);
        assertThat(exception.getMessage()).isEqualTo("Invalid credentials");
        verifyNoInteractions(jwtUtils, userRepository);
    };

    @Test
    @DisplayName("Register User -> Success")
    public void createAndSaveNewUser_withSuccess() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("FirstName", savedUser.getFirstName());
        assertEquals("LastName", savedUser.getLastName());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertNotNull(response.getBody());
        assertInstanceOf(MessageResponse.class, response.getBody());
    };

    @Test
    @DisplayName("Register User -> Failure (Email Already Taken)")
    public void shouldReturnBadRequestWhenEmailIsTaken() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertInstanceOf(MessageResponse.class, response.getBody());
    };
}
