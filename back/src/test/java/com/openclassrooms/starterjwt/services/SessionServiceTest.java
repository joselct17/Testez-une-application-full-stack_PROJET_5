package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @InjectMocks
    SessionService sessionService;

    @Mock
    SessionRepository sessionRepository;

    @Mock
    UserRepository userRepository;

    @Test
    void shouldCallSaveFromSessionRepository() {
        // Given
        Session session = new Session();
        // When
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        Session session1 = sessionService.create(session);
        // Then
        verify(sessionRepository, Mockito.times(1)).save(any(Session.class));
        assertEquals(session, session1);
    }

    @Test
    void shouldCallDeleteByIdFromSessionRepository() {
        // Given
        Long id = 1L;
        // When
        sessionService.delete(id);
        // Then
        verify(sessionRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void shouldCallFindAllFromSessionRepository() {
        // Given
        List<Session> sessions = List.of(new Session());
        // When
        when(sessionRepository.findAll()).thenReturn(sessions);
        List<Session> sessions1 = sessionService.findAll();
        // Then
        verify(sessionRepository, Mockito.times(1)).findAll();
        assertEquals(sessions, sessions1);
    }

    @Test
    void shouldCallFindByIdFromSessionRepository() {
        // Given
        Long id = 1L;
        Session session = new Session();
        // When
        when(sessionRepository.findById(any(Long.class))).thenReturn(Optional.of(session));
        Session session1 = sessionService.getById(id);
        // Then
        verify(sessionRepository, Mockito.times(1)).findById(id);
        assertEquals(session, session1);
    }

    @Test
    void shouldCallSaveFromSessionRepositoryWithId() {
        // Given
        Long id = 1L;
        Session session = new Session();
        // When
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        Session session1 = sessionService.update(id, session);
        // Then
        verify(sessionRepository, Mockito.times(1)).save(any(Session.class));
        assertEquals(session, session1);
    }

    @Test
    void shouldCallSaveFromSessionRepositoryInParticipate() {
        // Given
        Long id = 1L;
        Long userId = 1L;
        Session session = new Session();
        User user = new User();
        user.setId(3L);
        List<User> users = new ArrayList<>();
        users.add(user);
        session.setUsers(users);
        // When
        when(sessionRepository.findById(any(Long.class))).thenReturn(Optional.of(session));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        sessionService.participate(id, userId);
        // Then
        verify(sessionRepository, Mockito.times(1)).save(any(Session.class));
        assertEquals(session.getUsers().size(), 2);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserIsNotFound() {
        // Given
        Long id = 1L;
        Long userId = 1L;
        Session session = new Session();
        // When
        when(sessionRepository.findById(any(Long.class))).thenReturn(Optional.of(session));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        // Then
        assertThrows(NotFoundException.class, () -> sessionService.participate(id, userId));
    }


    @Test
    void shouldThrowBadRequestExceptionWhenAlreadyParticipateForParticipate() {
        // Given
        Long id = 1L;
        Long userId = 1L;
        Session session = new Session();
        User user = new User();
        user.setId(userId);
        session.setUsers(List.of(user));
        // When
        when(sessionRepository.findById(any(Long.class))).thenReturn(Optional.of(session));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        // Then
        assertThrows(BadRequestException.class, () -> sessionService.participate(id, userId));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSessionIsNotFoundForNoLongerParticipate() {
        // Given
        Long id = 1L;
        Long userId = 1L;
        // When
        when(sessionRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        // Then
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(id, userId));
    }

    @Test
    void shouldCallSaveFromSessionRepositoryInNoLongerParticipate() {
        // Given
        Long id = 1L;
        Long userId = 1L;
        Session session = new Session();
        User user = new User();
        user.setId(userId);
        session.setUsers(List.of(user));
        // When
        when(sessionRepository.findById(any(Long.class))).thenReturn(Optional.of(session));
        sessionService.noLongerParticipate(id, userId);
        // Then
        verify(sessionRepository, Mockito.times(1)).save(any(Session.class));
        assertEquals(session.getUsers().size(), 0);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenNotAlreadyParticipateForNoLongerParticipate() {
        // Given
        Long id = 1L;
        Long userId = 1L;
        Session session = new Session();
        User user = new User();
        user.setId(4L);
        session.setUsers(List.of(user));
        // When
        when(sessionRepository.findById(any(Long.class))).thenReturn(Optional.of(session));
        // Then
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(id, userId));
    }


}