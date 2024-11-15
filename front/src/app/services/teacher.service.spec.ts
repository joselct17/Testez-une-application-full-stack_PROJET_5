import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TeacherService],
    });

    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Ensure no outstanding HTTP requests
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });

  it('should retrieve all teachers', (done) => {
    const mockTeachers: Teacher[] = [
      { id: 1, lastName: 'Doe', firstName: 'John' , createdAt:new Date("2024-11-13T12:25:25.475Z"), updatedAt:new Date("2024-11-13T12:25:25.475Z")},
      { id: 2, lastName: 'Smith', firstName: 'Jane' , createdAt:new Date("2024-11-13T12:25:25.475Z"), updatedAt:new Date("2024-11-13T12:25:25.475Z")},
    ];

    service.all().subscribe((teachers) => {
      expect(teachers).toEqual(mockTeachers);
      done()
    });

    const req = httpMock.expectOne('api/teacher');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeachers);
  });

  it('should retrieve a teacher by ID', (done) => {
    const mockTeacher: Teacher =       { id: 1, lastName: 'Doe', firstName: 'John' , createdAt:new Date("2024-11-13T12:25:25.475Z"), updatedAt:new Date("2024-11-13T12:25:25.475Z")};

    service.detail('1').subscribe((teacher) => {
      expect(teacher).toEqual(mockTeacher);
      done()
    });

    const req = httpMock.expectOne('api/teacher/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeacher);
  });

  it('should handle 404 error when retrieving a teacher by ID', () => {
    service.detail('999').subscribe(
      () => fail('should have failed with a 404 error'),
      (error) => {
        expect(error.status).toBe(404);
      }
    );

    const req = httpMock.expectOne('api/teacher/999');
    expect(req.request.method).toBe('GET');
    req.flush('Teacher not found', { status: 404, statusText: 'Not Found' });
  });
});

