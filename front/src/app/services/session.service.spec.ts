import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;
  let mockSessionInfo: SessionInformation;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
    mockSessionInfo = {
      token: 'token',
      type: 'type',
      id: 1,
      username: 'username',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true,
    };
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should start with isLogged as false and no sessionInformation', () => {
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should correctly log in a user, set sessionInformation, and emit the logged in status', () => {
    let isLoggedValue: boolean | undefined;
    service.$isLogged().subscribe(isLogged => {
      isLoggedValue = isLogged;
    });
    service.logIn(mockSessionInfo);
    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockSessionInfo);
    expect(isLoggedValue).toBeTruthy();
  });

  it('should log out a user, clear sessionInformation, and emit the logged out status', () => {
    let isLoggedValue: boolean | undefined;
    service.logIn(mockSessionInfo);
    service.$isLogged().subscribe(isLogged => {
      isLoggedValue = isLogged;
    });
    service.logOut();
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
    expect(isLoggedValue).toBeFalsy();
  });

  it('should handle session information updates on login and logout', () => {
    service.logIn(mockSessionInfo);
    expect(service.sessionInformation).toEqual(mockSessionInfo);
    service.logOut();
    expect(service.sessionInformation).toBeUndefined();
  });
});
