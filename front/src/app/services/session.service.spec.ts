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

  it('should start with isLogged is false', (done) => {
    expect(service.isLogged).toBeFalsy()
    done()
  })

  it('should set user into sessionInformation and isLogged to true', (done) => {
    const mockSession: SessionInformation =  {
      token: '1234',
      type: 'Bearer',
      id: 1,
      username: 'JohnDoe',
      firstName: 'John',
      lastName: 'Doe',
      admin: false
    };
    service.logIn(mockSession);
    expect(service.isLogged).toBeTruthy()
    expect(service.sessionInformation).toEqual(mockSession)
    done()
  })

  it('should set isLogged to false and sessionInformation to undefined', (done) => {
    const mockSession: SessionInformation =  {
      token: '1234',
      type: 'Bearer',
      id: 1,
      username: 'JohnDoe',
      firstName: 'John',
      lastName: 'Doe',
      admin: false
    };
    service.logIn(mockSession);
    service.logOut()
    expect(service.sessionInformation).toBeUndefined()
    expect(service.isLogged).toBeFalsy()
    done()
  })

  // Manage async test with done method
  it('should emit correct value from observable $isLogged', (done) => {
    const mockSession: SessionInformation =  {
      token: '1234',
      type: 'Bearer',
      id: 1,
      username: 'JohnDoe',
      firstName: 'John',
      lastName: 'Doe',
      admin: false
    };

    let asyncCheck = false
    service.$isLogged().subscribe(isLogged => {
      if(!asyncCheck){
        expect(isLogged).toBeFalsy()
        asyncCheck=true
        service.logIn(mockSession);
      }
      else {
        expect(isLogged).toBeTruthy()
        done()
      }
    })
  })
});
