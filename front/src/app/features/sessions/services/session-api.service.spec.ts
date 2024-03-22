import { Observable } from 'rxjs';
import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SessionApiService } from './session-api.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('SessionsService', () => {
  let service: SessionApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(SessionApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  const mockSession1 : SessionInformation = { 
    token: '1',
    type: '1',
    id: 1,
    username: '1',
    firstName: '1',
    lastName: '1',
    admin: true
   };
  const mockSession2 : SessionInformation = { 
    token: '2',
    type: '2',
    id: 2,
    username: '2',
    firstName: '2',
    lastName: '2',
    admin: false
   };
  const mockSession3 : SessionInformation = { 
    token: '3',
    type: '3',
    id: 3,
    username: '3',
    firstName: '3',
    lastName: '3',
    admin: true
   };

  it('should get all sessions', () => {
    const mockSessionArray = [mockSession1, mockSession2, mockSession3]
    const observable = service.all();
    let sessionSubscribe;
    observable.subscribe(sessions => {
      sessionSubscribe = sessions;
    });
    expect(observable).toBeTruthy();
    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessionArray);
    expect(sessionSubscribe).toEqual(mockSessionArray);
  });

  it('should get session detail', () => {
    const observable = service.detail('1');
    let sessionSubscribe;
    observable.subscribe(session => {
      sessionSubscribe = session;
    });
    expect(observable).toBeTruthy();
    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSession1);
    expect(sessionSubscribe).toEqual(mockSession1);
  });

  it('should delete session', () => {
    const observable = service.delete('1');
    let sessionSubscribe;
    observable.subscribe(session => {
      sessionSubscribe = session;
    });
    expect(observable).toBeTruthy();
    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(mockSession1);
    expect(sessionSubscribe).toEqual(mockSession1);
  });

  it('should create session', () => {
    const observable = service.create({} as any);
    let sessionSubscribe;
    observable.subscribe(session => {
      sessionSubscribe = session;
    });
    expect(observable).toBeTruthy();
    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    req.flush(mockSession1);
    expect(sessionSubscribe).toEqual(mockSession1);
  });

  it('should update session', () => {
    const observable = service.update('1', {} as any);
    let sessionSubscribe;
    observable.subscribe(session => {
      sessionSubscribe = session;
    });
    expect(observable).toBeTruthy();
    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    req.flush(mockSession1);
    expect(sessionSubscribe).toEqual(mockSession1);
  });

  it('should participate in the session', () => {
    const observable = service.participate('1', '1');
    let sessionSubscribe;
    observable.subscribe(session => {
      sessionSubscribe = session;
    });
    expect(observable).toBeTruthy();
    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne('api/session/1/participate/1');
    expect(req.request.method).toBe('POST');
    req.flush(mockSession1);
    expect(sessionSubscribe).toEqual(mockSession1);
  });

  it('should unparticipate in the session', () => {
    const observable = service.unParticipate('1', '1');
    let sessionSubscribe;
    observable.subscribe(session => {
      sessionSubscribe = session;
    });
    expect(observable).toBeTruthy();
    const httpMock = TestBed.inject(HttpTestingController);
    const req = httpMock.expectOne('api/session/1/participate/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(mockSession1);
    expect(sessionSubscribe).toEqual(mockSession1);
  });
});
