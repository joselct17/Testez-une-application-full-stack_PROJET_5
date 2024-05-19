package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Tests for SessionService")
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    private SessionService sessionService;

    private Session session;
    private User user;

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

        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("lastname")
                .firstName("firstname")
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();

        session = Session.builder()
                .name("name")
                .id(1L)
                .date(Date.from(Instant.now()))
                .description("description")
                .teacher(teacher)
                .users(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .build();

        sessionService = new SessionService(sessionRepository, userRepository);
    };

    @Test
    @DisplayName("Create Session → Saves and Returns Session")
    public void testCreateSession() {
        // Arrange
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Act
        Session savedSession = sessionService.create(new Session());

        // Assert
        verify(sessionRepository, times(1)).save(any(Session.class));
        assertEquals(savedSession, session);
    };

    @Test
    @DisplayName("Delete Session by ID")
    public void testDeleteSessionById() {
        // Act
        sessionService.delete(session.getId());

        // Assert
        verify(sessionRepository, times(1)).deleteById(session.getId());
    };

    @Test
    @DisplayName("Find All Sessions → Returns All Sessions")
    public void testFindAllSessions() {
        // Arrange
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(session));

        // Act
        List<Session> sessions = sessionService.findAll();

        // Assert
        verify(sessionRepository, times(1)).findAll();
        assertFalse(sessions.isEmpty());
        assertEquals(sessions.size(), 1);
        assertEquals(sessions.get(0), session);
    };

    @Test
    @DisplayName("Find All Sessions → Returns No Sessions")
    public void testFindAllSessions_NoSessions() {
        // Arrange
        when(sessionRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Session> sessions = sessionService.findAll();

        // Assert
        verify(sessionRepository, times(1)).findAll();
        assertTrue(sessions.isEmpty());
    };

    @Test
    @DisplayName("Find Session by ID → Returns Session")
    public void testFindSessionById() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        // Act
        Session foundSession = sessionService.getById(1L);

        // Assert
        verify(sessionRepository, times(1)).findById(1L);
        assertNotNull(foundSession);
        assertEquals(foundSession, session);
    };

    @Test
    @DisplayName("Find Session by ID → Returns Null for Non-Existing ID")
    public void testFindSessionById_NonExistingId() {
        // Arrange
        when(sessionRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        Session result = sessionService.getById(2L);

        // Assert
        verify(sessionRepository, times(1)).findById(2L);
        assertNull(result);
    };

    @Test
    @DisplayName("Update Session → Returns Updated Session")
    public void testUpdateSession() {
        // Arrange
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Act
        Session updatedSession = sessionService.update(session.getId(), new Session());

        // Assert
        verify(sessionRepository, times(1)).save(any(Session.class));
        assertEquals(updatedSession.getId(), session.getId());
    };

    @Test
    @DisplayName("Participate in Session → Successful Participation")
    public void testParticipate_Success() {
        // Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        sessionService.participate(session.getId(), user.getId());

        // Assert
        verify(sessionRepository, times(1)).save(any(Session.class));
        assertTrue(session.getUsers().contains(user));
    };

    @Test
    @DisplayName("Participate in Session → User Not Found")
    public void testParticipate_UserNotFound() {
        // Arrange
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.participate(session.getId(), user.getId()));

        verify(sessionRepository, times(1)).findById(session.getId());
        verify(userRepository, times(1)).findById(user.getId());
    };

    @Test
    @DisplayName("Participate in Session → Session Not Found")
    public void testParticipate_SessionNotFound() {
        // Arrange
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.participate(session.getId(), user.getId()));

        verify(sessionRepository, times(1)).findById(session.getId());
    };

    @Test
    @DisplayName("No Longer Participate in Session → Successful")
    public void testNoLongerParticipate_Success() {
        // Arrange
        session.getUsers().add(user);
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Act
        sessionService.noLongerParticipate(session.getId(), user.getId());

        // Assert
        verify(sessionRepository, times(1)).findById(session.getId());
        verify(sessionRepository, times(1)).save(session);
        assertFalse(session.getUsers().contains(user));
    };

    @Test
    @DisplayName("No Longer Participate in Session → User Not Found")
    public void testNoLongerParticipate_UserNotFound() {
        // Arrange
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        user.setId(5L);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(session.getId(), user.getId()));

        verify(sessionRepository, times(1)).findById(session.getId());
    };

    @Test
    @DisplayName("No Longer Participate in Session → Session Not Found")
    public void testNoLongerParticipate_SessionNotFound() {
        // Arrange
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(session.getId(), user.getId()));

        verify(sessionRepository, times(1)).findById(session.getId());
    }
}
