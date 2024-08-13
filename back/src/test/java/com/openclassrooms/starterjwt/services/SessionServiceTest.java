package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

  @Mock
  private SessionRepository sessionRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private SessionService classUnderTest;

  @Test
  public void create_ShouldReturnSession() {
    Session session = new Session();
    when(sessionRepository.save(session)).thenReturn(session);

    Session returnedSession = classUnderTest.create(session);

    verify(sessionRepository, times(1)).save(session);
    assertThat(returnedSession).isNotNull();
  }

  @Test
  public void delete_ShouldUseRepository() {
    willDoNothing().given(sessionRepository).deleteById(1L);

    classUnderTest.delete(1L);

    verify(sessionRepository, times(1)).deleteById(1L);
  }

  @Test
  public void findAll_ShouldReturnSessions() {
    when(sessionRepository.findAll()).thenReturn(List.of(new Session(), new Session()));

    List<Session> sessions = classUnderTest.findAll();

    verify(sessionRepository, times(1)).findAll();
    assertThat(sessions).isNotNull();
    assertThat(sessions.size()).isEqualTo(2);
  }

  @Test
  public void getById_shouldReturnASession() {
    when(sessionRepository.findById(1L)).thenReturn(Optional.of(new Session()));

    Session session = classUnderTest.getById(1L);

    verify(sessionRepository, times(1)).findById(1L);
    assertThat(session).isNotNull();
  }

  @Test
  public void update_shouldUpdateId() {
    Session session = new Session();
    session.setId(1L);
    when(sessionRepository.save(session)).thenReturn(session);

    Session returnedSession = classUnderTest.update(2L, session);

    verify(sessionRepository, times(1)).save(session);
    assertThat(returnedSession).isNotNull();
    assertThat(returnedSession.getId()).isEqualTo(2L);
  }

  @Test
  public void participate_throwsIfAlreadyParticipate() {
    User user = new User();
    user.setId(1L);

    Session session = new Session();
    session.setUsers(Arrays.asList(user));

    when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    assertThrows(BadRequestException.class, () -> classUnderTest.participate(1L, 1L));
  }

  @Test
  public void participate_throwsIfSessionDoesntExist() {
    when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
    when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

    assertThrows(NotFoundException.class, () -> classUnderTest.participate(1L, 1L));
  }

  @Test
  public void participate_throwsIfUserDoesntExist() {
    when(sessionRepository.findById(1L)).thenReturn(Optional.of(new Session()));
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> classUnderTest.participate(1L, 1L));
  }

  @Test
  public void participate_shouldAddUserToSession() {
    User user = new User();
    user.setId(1L);

    Session session = new Session();
    session.setUsers(new ArrayList<>());

    when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    classUnderTest.participate(1L, 1L);

    assertThat(session.getUsers()).contains(user);

    verify(sessionRepository, times(1)).findById(1L);
    verify(userRepository, times(1)).findById(1L);
    verify(sessionRepository, times(1)).save(session);
  }
  
  @Test
  public void noLongerParticipate_throwsIfSessionDoesntExist() {
    when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> classUnderTest.noLongerParticipate(1L, 1L));
  }
  
  @Test
  public void noLongerParticipate_throwsIfNotParticipating() {
    User user = new User();
    user.setId(1L);

    Session session = new Session();
    session.setUsers(new ArrayList<>());

    when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

    assertThrows(BadRequestException.class, () -> classUnderTest.noLongerParticipate(1L, 1L));
  }
  
  @Test
  public void noLongerParticipate_shouldRemoveUserFromSession() {
    User user = new User();
    user.setId(1L);
    User user2 = new User();
    user2.setId(2L);
    Session session = new Session();
    session.setUsers(Arrays.asList(user2, user));

    when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
    
    classUnderTest.noLongerParticipate(1L, 1L);
    
    verify(sessionRepository, times(1)).save(session);
    assertThat(session.getUsers()).doesNotContain(user);
  }
}
