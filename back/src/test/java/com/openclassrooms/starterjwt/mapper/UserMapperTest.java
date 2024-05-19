package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests for UserMapper")
public class UserMapperTest {

    private UserMapper userMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);

        user = User.builder()
                .id(1L)
                .email("test@email.fr")
                .lastName("lastName")
                .firstName("firstName")
                .password("password")
                .admin(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userDto = new UserDto(
                1L,
                "test@email.fr",
                "lastName",
                "firstName",
                false,
                "password",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    };

    @Test
    @DisplayName("toEntity should map UserDto to User correctly")
    public void testToEntity() {
        // Act
        User result = userMapper.toEntity(userDto);

        // Assert
        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(userDto.getLastName(), result.getLastName());
        assertEquals(userDto.getFirstName(), result.getFirstName());
        assertEquals(userDto.getPassword(), result.getPassword());
        assertEquals(userDto.isAdmin(), result.isAdmin());
        assertEquals(userDto.getCreatedAt(), result.getCreatedAt());
        assertEquals(userDto.getUpdatedAt(), result.getUpdatedAt());
    };

    @Test
    @DisplayName("toDto should map User to UserDto correctly")
    public void testToDto() {
        // Act
        UserDto result = userMapper.toDto(user);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.isAdmin(), result.isAdmin());
        assertEquals(user.getCreatedAt(), result.getCreatedAt());
        assertEquals(user.getUpdatedAt(), result.getUpdatedAt());
    };

    @Test
    @DisplayName("toEntityList should map list of UserDto to list of User correctly")
    public void testToEntityList() {
        // Arrange
        List<UserDto> userDtoList = Collections.singletonList(userDto);

        // Act
        List<User> result = userMapper.toEntity(userDtoList);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userDto.getId(), result.get(0).getId());
        assertEquals(userDto.getEmail(), result.get(0).getEmail());
        assertEquals(userDto.getLastName(), result.get(0).getLastName());
        assertEquals(userDto.getFirstName(), result.get(0).getFirstName());
        assertEquals(userDto.getPassword(), result.get(0).getPassword());
        assertEquals(userDto.isAdmin(), result.get(0).isAdmin());
        assertEquals(userDto.getCreatedAt(), result.get(0).getCreatedAt());
        assertEquals(userDto.getUpdatedAt(), result.get(0).getUpdatedAt());
    };

    @Test
    @DisplayName("toDtoList should map list of User to list of UserDto correctly")
    public void testToDtoList() {
        // Arrange
        List<User> teacherList = Collections.singletonList(user);

        // Act
        List<UserDto> result = userMapper.toDto(teacherList);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).getId());
        assertEquals(user.getLastName(), result.get(0).getLastName());
        assertEquals(user.getFirstName(), result.get(0).getFirstName());
        assertEquals(user.getCreatedAt(), result.get(0).getCreatedAt());
        assertEquals(user.getUpdatedAt(), result.get(0).getUpdatedAt());
    };
}