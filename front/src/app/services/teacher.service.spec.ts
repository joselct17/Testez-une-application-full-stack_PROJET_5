import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(TeacherService);
  });
  const mockTeacher1: Teacher = {
    id : 1,
    lastName : '1',
    firstName : '1',
    createdAt : new Date(),
    updatedAt : new Date()
  };

  const mockTeacher2: Teacher = {
    id : 2,
    lastName : '2',
    firstName : '2',
    createdAt : new Date(),
    updatedAt : new Date()
  };

  const mockTeacher3: Teacher = {
    id : 3,
    lastName : '3',
    firstName : '3',
    createdAt : new Date(),
    updatedAt : new Date()
  };

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all teachers', () => {
    const mockTeacherArray = [mockTeacher1, mockTeacher2, mockTeacher3]
    const observable = service.all();
    let teacherSubscribe;
    observable.subscribe(observable => {
      teacherSubscribe = observable;
    });
    expect(observable).toBeTruthy();
    const httpmock = TestBed.inject(HttpTestingController);
    const req = httpmock.expectOne('api/teacher');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeacherArray);
    expect(teacherSubscribe).toEqual(mockTeacherArray);
  });

  it('should get teacher detail', () => {
    const observable = service.detail('1');
    let teacherSubscribe;
    observable.subscribe(observable => {
      teacherSubscribe = observable;
    });
    expect(observable).toBeTruthy();
    const httpmock = TestBed.inject(HttpTestingController);
    const req = httpmock.expectOne('api/teacher/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeacher1);
    expect(teacherSubscribe).toEqual(mockTeacher1);
  });
});
