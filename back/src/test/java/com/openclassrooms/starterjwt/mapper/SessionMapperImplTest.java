package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests for SessionMapperImpl")
public class SessionMapperImplTest {

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private UserService userService;

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    private SessionDto sessionDto;
    private Session session;
    private Teacher teacher;
    private User user;

    @BeforeEach
    public void setup() throws ParseException {
        sessionDto = new SessionDto(
                1L,
                "sessionName",
                new Date(),
                1L,
                "description",
                List.of(1L),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        teacher = Teacher.builder()
                .id(1L)
                .lastName("lastName")
                .firstName("firstname")
                .build();
        user = User.builder()
                .id(1L)
                .email("test@email.fr")
                .firstName("firstname")
                .lastName("lastname")
                .password("password")
                .build();
        session = Session.builder()
                .id(1L)
                .name("sessionName")
                .date(new Date())
                .teacher(teacher)
                .users(List.of(user))
                .description("description")
                .build();

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user);
    };

    @Test
    @DisplayName("toEntity should map SessionDto to Session correctly")
    public void testToEntity() {
        // Act
        Session sessionMapped = sessionMapper.toEntity(sessionDto);

        // Assert
        assertEquals(session.getId(), sessionMapped.getId());
        assertThat(session.getUsers().get(0).getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("toEntity should handle null Teacher ID correctly")
    public void testToEntity_NullTeacherId() {
        // Arrange
        sessionDto.setTeacher_id(null);

        // Act
        Session result = sessionMapper.toEntity(sessionDto);

        // Assert
        assertNotNull(result);
        assertNull(result.getTeacher());
    };

    @Test
    @DisplayName("toEntity should handle null User list correctly")
    public void testToEntity_NullUserList() {
        // Arrange
        sessionDto.setUsers(null);

        // Act
        Session result = sessionMapper.toEntity(sessionDto);

        // Assert
        assertNotNull(result);
        assertTrue(result.getUsers().isEmpty());
    };

    @Test
    @DisplayName("toDto should map Session to SessionDto correctly")
    public void testToDto() {
        // Act
        SessionDto result = sessionMapper.toDto(session);

        // Assert
        assertNotNull(result);
        assertEquals(session.getDescription(), result.getDescription());
        assertEquals(session.getTeacher().getId(), result.getTeacher_id());
        assertEquals(1, result.getUsers().size());
        assertEquals(user.getId(), result.getUsers().get(0));
    };

    @Test
    @DisplayName("toDto should handle null Teacher correctly")
    public void testToDto_NullTeacher() {
        // Arrange
        session.setTeacher(null);

        // Act
        SessionDto result = sessionMapper.toDto(session);

        // Assert
        assertNotNull(result);
        assertNull(result.getTeacher_id());
    };

    @Test
    @DisplayName("toDto should handle null User list correctly")
    public void testToDto_NullUserList() {
        // Arrange
        session.setUsers(null);

        // Act
        SessionDto result = sessionMapper.toDto(session);

        // Assert
        assertNotNull(result);
        assertTrue(result.getUsers().isEmpty());
    };

    @Test
    @DisplayName("toEntity List should map list of SessionDto to list of Session correctly")
    public void testToEntityList() {
        // Arrange
        List<SessionDto> sessionDtos = Collections.singletonList(sessionDto);

        // Act
        List<Session> result = sessionMapper.toEntity(sessionDtos);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sessionDto.getDescription(), result.get(0).getDescription());
    };

    @Test
    @DisplayName("toDto List should map list of Session to list of SessionDto correctly")
    public void testToDtoList() {
        // Arrange
        List<Session> sessions = Collections.singletonList(session);

        // Act
        List<SessionDto> result = sessionMapper.toDto(sessions);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(session.getDescription(), result.get(0).getDescription());
    };

    @Test
    @DisplayName("toEntity should handle empty list correctly")
    public void testToEntity_EmptyList() {
        // Act
        List<Session> result = sessionMapper.toEntity(Collections.emptyList());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    };

    @Test
    @DisplayName("toDto should handle empty list correctly")
    public void testToDto_EmptyList() {
        // Act
        List<SessionDto> result = sessionMapper.toDto(Collections.emptyList());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    };

    @Test
    @DisplayName("whenSessionToSessionDto_thenEquals")
    public void whenSessionToSessionDto_thenEquals() {
        // Arrange
        Session givenSession = new Session();
        givenSession.setId(3L);
        givenSession.setName("Session name");
        givenSession.setDate(new Date());
        givenSession.setTeacher(new Teacher(3L, "lastName", "firstName", LocalDateTime.now(), null));
        givenSession.setDescription("Description");
        givenSession.setCreatedAt(LocalDateTime.now());
        givenSession.setUpdatedAt(null);

        User givenUser = new User();
        givenUser.setId(4L);
        givenUser.setEmail("test@mail.fr");
        givenUser.setLastName("LastName");
        givenUser.setFirstName("FirstName");
        givenUser.setPassword("Password");
        givenUser.setAdmin(true);
        givenUser.setCreatedAt(LocalDateTime.now());
        givenUser.setUpdatedAt(null);

        ArrayList<User> users = new ArrayList<>();
        users.add(givenUser);
        givenSession.setUsers(users);

        // Act
        SessionDto sessionDto = sessionMapper.toDto(givenSession);

        // Assert
        assertEquals(givenSession.getId(), sessionDto.getId());
        assertEquals(givenSession.getName(), sessionDto.getName());
        assertEquals(givenSession.getDescription(), sessionDto.getDescription());
        assertEquals(givenSession.getCreatedAt(), sessionDto.getCreatedAt());
        assertEquals(givenSession.getUsers().size(), sessionDto.getUsers().size());
        assertEquals(givenSession.getTeacher().getId(), sessionDto.getTeacher_id());
    };

    @Test
    @DisplayName("whenSessionDtoToSession_thenEquals")
    public void whenSessionDtoToSession_thenEquals() {
        // Arrange
        SessionDto givenSessionDto = new SessionDto();
        givenSessionDto.setId(3L);
        givenSessionDto.setName("Session Name");
        givenSessionDto.setDate(new Date());
        givenSessionDto.setTeacher_id(3L);
        givenSessionDto.setDescription("Session description");
        givenSessionDto.setCreatedAt(LocalDateTime.now());
        givenSessionDto.setUpdatedAt(null);

        User givenUser = new User();
        givenUser.setId(99L);
        givenUser.setEmail("mail@mail.fr");
        givenUser.setLastName("LastName");
        givenUser.setFirstName("FirstName");
        givenUser.setPassword("Password");
        givenUser.setAdmin(true);
        givenUser.setCreatedAt(LocalDateTime.now());
        givenUser.setUpdatedAt(null);

        ArrayList<Long> userIds = new ArrayList<>();
        userIds.add(givenUser.getId());
        givenSessionDto.setUsers(userIds);

        // Act
        Session session = sessionMapper.toEntity(givenSessionDto);

        // Assert
        assertEquals(givenSessionDto.getId(), session.getId());
        assertEquals(givenSessionDto.getName(), session.getName());
        assertEquals(givenSessionDto.getDescription(), session.getDescription());
        assertEquals(givenSessionDto.getCreatedAt(), session.getCreatedAt());
        assertEquals(givenSessionDto.getUsers().size(), session.getUsers().size());
    };
}
