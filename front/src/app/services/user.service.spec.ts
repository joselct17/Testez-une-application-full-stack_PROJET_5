import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let service: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(UserService);
  });

  const mockUser1 : User = {
    id: 1,
    email: '1',
    lastName: '1',
    firstName: '1',
    admin: true,
    password: '1',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get user by id', () => {
    const observable = service.getById('1');
    let userSubscribe;
    observable.subscribe(user => {
      userSubscribe = user;
    });
    expect(observable).toBeTruthy();
    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne('api/user/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockUser1);
    expect(userSubscribe).toEqual(mockUser1);
  });

  it('should delete user', () => {
    const observable = service.delete('1');
    let userSubscribe;
    observable.subscribe(user => {
      userSubscribe = user;
    });
    expect(observable).toBeTruthy();
    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne('api/user/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(mockUser1);
    expect(userSubscribe).toEqual(mockUser1);
  });
});
