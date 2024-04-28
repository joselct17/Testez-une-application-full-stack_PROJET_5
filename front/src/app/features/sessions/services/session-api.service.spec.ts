import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { expect } from '@jest/globals';
import { SessionApiService } from './session-api.service';
import { Session } from "../interfaces/session.interface";

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpTestingController: HttpTestingController;
  let sessionMock: Session;
  const apiBaseUrl = 'api/session';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService],
    });

    service = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);

    sessionMock = {
      id: 1,
      name: 'name',
      description: 'description',
      date: new Date(),
      teacher_id: 1,
      users: [1, 2],
      createdAt: new Date('2024-01-01'),
      updatedAt: new Date('2024-01-01'),
    };
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('CRUD Operations', () => {
    it('should get all sessions', () => {
      service.all().subscribe((response) => {
        expect(response).toEqual([sessionMock]);
      });

      const req = httpTestingController.expectOne(apiBaseUrl);
      expect(req.request.method).toBe('GET');
      req.flush([sessionMock]);
    });

    it('should get session detail by id', () => {
      service.detail('1').subscribe((session) => {
        expect(session).toEqual(sessionMock);
      });

      const req = httpTestingController.expectOne(`${apiBaseUrl}/1`);
      expect(req.request.method).toBe('GET');
      req.flush(sessionMock);
    });

    it('should delete session by id', () => {
      service.delete('1').subscribe((response) => {
        expect(response).toEqual({});
      });

      const req = httpTestingController.expectOne(`${apiBaseUrl}/1`);
      expect(req.request.method).toBe('DELETE');
      req.flush({});
    });

    it('should create a new session', () => {
      service.create(sessionMock).subscribe((session) => {
        expect(session).toEqual(sessionMock);
      });

      const req = httpTestingController.expectOne(apiBaseUrl);
      expect(req.request.method).toBe('POST');
      req.flush(sessionMock);
    });

    it('should update session by id', () => {
      service.update('1', sessionMock).subscribe((updatedSession) => {
        expect(updatedSession).toEqual(sessionMock);
      });

      const req = httpTestingController.expectOne(`${apiBaseUrl}/1`);
      expect(req.request.method).toBe('PUT');
      req.flush(sessionMock);
    });
  });

  describe('Participation', () => {
    it('should allow user to participate in session', () => {
      service.participate('1', '2').subscribe((response) => {
        expect(response).toEqual({});
      });

      const req = httpTestingController.expectOne(`${apiBaseUrl}/1/participate/2`);
      expect(req.request.method).toBe('POST');
      req.flush({});
    });

    it('should allow user to unparticipate from session', () => {
      service.unParticipate('1', '2').subscribe((response) => {
        expect(response).toEqual({});
      });

      const req = httpTestingController.expectOne(`${apiBaseUrl}/1/participate/2`);
      expect(req.request.method).toBe('DELETE');
      req.flush({});
    });
  });
});
