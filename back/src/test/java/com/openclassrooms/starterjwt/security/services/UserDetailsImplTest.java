package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests for UserDetailsImpl")
public class UserDetailsImplTest {

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .admin(true)
                .password("password")
                .build();
    };

    @Test
    @DisplayName("User Details → Correct Attributes")
    public void userDetails_CorrectAttributes() {
        // Assert
        assertEquals(Long.valueOf(1), userDetails.getId());
        assertEquals("username", userDetails.getUsername());
        assertEquals("firstName", userDetails.getFirstName());
        assertEquals("lastName", userDetails.getLastName());
        assertTrue(userDetails.getAdmin());
        assertEquals("password", userDetails.getPassword());
    };

    @Test
    @DisplayName("User Details → Account Status")
    public void userDetails_AccountStatus() {
         // Assert
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    };

    @Test
    @DisplayName("User Details → Authorities")
    public void userDetails_Authorities() {
        // Act
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    };

    @Test
    @DisplayName("User Details → Equality")
    public void userDetails_Equality() {
        // Arrange
        UserDetailsImpl sameUser = UserDetailsImpl.builder()
                .id(1L)
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .admin(true)
                .password("password")
                .build();

        UserDetailsImpl differentUser = UserDetailsImpl.builder()
                .id(2L)
                .build();

        // Assert
        assertEquals(userDetails, sameUser);
        assertNotEquals(userDetails, differentUser);
        assertNotEquals(userDetails, null);
    };
}
