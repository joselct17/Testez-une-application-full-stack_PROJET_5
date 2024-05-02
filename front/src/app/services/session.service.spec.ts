import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log in', () => {
    const user: SessionInformation = {
      token: 'token',
      type: 'type',
      id: 1,
      username: 'username',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true
    };
    service.logIn(user);
    expect(service.isLogged).toBeTruthy();
  });

  it('should log out', () => {
    service.logOut();
    expect(service.isLogged).toBeFalsy();
  });

  it('should return isLogged', () => {
    service.isLogged = true;
    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBeTruthy();
    });
  });
});
