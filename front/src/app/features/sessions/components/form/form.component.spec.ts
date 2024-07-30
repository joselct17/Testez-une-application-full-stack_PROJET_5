import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { of } from 'rxjs';
import { TeacherService } from 'src/app/services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { NgZone } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let ngZone: NgZone;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }
  
  const mockTeachers = [
    { id: 1, firstName: 'John', lastName: 'Doe' },
    { id: 2, firstName: 'Jane', lastName: 'Smith' }
  ];

  const mockSessionApiService = {
    create: jest.fn().mockReturnValue(of({})),
    detail: jest.fn().mockReturnValue(of({})),
    update: jest.fn().mockReturnValue(of({})),
  };

  const mockTeacherService = {
    all: jest.fn().mockReturnValue(of(mockTeachers))
  };
  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('1')
      }
    }
  };
  const mockRouter = {
    url: '/sessions/create'
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
        NoopAnimationsModule
      ],
      providers: [
        SessionApiService,
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: Router, useValue: mockRouter }
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    ngZone = TestBed.inject(NgZone);
    fixture.detectChanges();
  });

  
  describe('Session Form Unit Test suite',()=> {
    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should show error if name is empty', ()=> {
      component.sessionForm?.controls['name'].setValue('');
 
      component.sessionForm?.controls['name'].markAsTouched();
      component.sessionForm?.controls['name'].updateValueAndValidity();

      fixture.detectChanges();

      const nameInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="name"]');

      expect(nameInput.classList).toContain('ng-invalid');
    })

    it('should show error if date is empty', ()=> {
      component.sessionForm?.controls['date'].setValue('');
 
      component.sessionForm?.controls['date'].markAsTouched();
      component.sessionForm?.controls['date'].updateValueAndValidity();

      fixture.detectChanges();

      const dateInput = fixture.debugElement.nativeElement.querySelector('input[formControlName="date"]');

      expect(dateInput.classList).toContain('ng-invalid');
    })

    it('should show error if teacher is empty', ()=> {
      component.sessionForm?.controls['teacher_id'].setValue('');
 
      component.sessionForm?.controls['teacher_id'].markAsTouched();
      component.sessionForm?.controls['teacher_id'].updateValueAndValidity();

      fixture.detectChanges();

      const teacherInput = fixture.debugElement.nativeElement.querySelector('mat-select[formControlName="teacher_id"]');

      expect(teacherInput.classList).toContain('ng-invalid');
    })

    it('should show error if description is empty', ()=> {
      component.sessionForm?.controls['description'].setValue('');
 
      component.sessionForm?.controls['description'].markAsTouched();
      component.sessionForm?.controls['description'].updateValueAndValidity();

      fixture.detectChanges();

      const descriptionInput = fixture.debugElement.nativeElement.querySelector('textarea[formControlName="description"]');

      expect(descriptionInput.classList).toContain('ng-invalid');
    })

    it('should show error if description is > 2000', ()=> {
      const longDescription = 'a'.repeat(2001);
      component.sessionForm?.controls['description'].setValue(longDescription);
 
      component.sessionForm?.controls['description'].markAsTouched();
      component.sessionForm?.controls['description'].updateValueAndValidity();

      fixture.detectChanges();

      const descriptionInput = fixture.debugElement.nativeElement.querySelector('textarea[formControlName="description"]');

      expect(descriptionInput.classList).toContain('ng-invalid');
    })

  })

  describe("Session Form Integration Test suite", ()=>{
    const sessionToCreate = {
      name: 'Yoga Session', 
      date: new Date().toISOString(), 
      description: 'A relaxing yoga session',
      teacher_id: 1
    }
    it('should create a session', () => {
      
      component.sessionForm?.controls['name'].setValue(sessionToCreate.name);
      component.sessionForm?.controls['date'].setValue(sessionToCreate.date);
      component.sessionForm?.controls['teacher_id'].setValue(sessionToCreate.teacher_id);
      component.sessionForm?.controls['description'].setValue(sessionToCreate.description);
      fixture.detectChanges();
      
      const submitButton = fixture.debugElement.nativeElement.querySelector('button[type="submit"]');
      ngZone.run(() => {
        submitButton.click();
      })
      fixture.detectChanges();
      
      expect(mockSessionApiService.create).toHaveBeenCalledWith(sessionToCreate);
      
      const snackBarContainer = document.querySelector('.mat-snack-bar-container');
      expect(snackBarContainer?.textContent).toContain('Session created !');
      
    });
    

    const sessionToEdit: Session = {
      id: 1,
      name: 'Yoga Session', 
      users: [], 
      date: new Date(), 
      description: 'A relaxing yoga session',
      createdAt: new Date(),
      updatedAt: new Date(),
      teacher_id: 1
    }
    const updatedSession = {
      ...sessionToEdit,
      name: 'Edited Yoga Session',
      description: 'Edited relaxing yoga session',
    }
  
    it("should edit session", ()=>{

      mockSessionApiService.detail = jest.fn().mockReturnValue(of(sessionToEdit));
      mockActivatedRoute.snapshot.paramMap.get = jest.fn().mockReturnValue(sessionToEdit.id?.toString());
      mockRouter.url = `/sessions/update/${sessionToEdit.id?.toString()}`;

      component.ngOnInit();
      fixture.detectChanges();

      expect(component.sessionForm?.value).toEqual({
        name: sessionToEdit.name,
        date: sessionToEdit.date.toISOString().split('T')[0],
        teacher_id: sessionToEdit.teacher_id,
        description: sessionToEdit.description,
      });

      component.sessionForm?.controls['name'].setValue(updatedSession.name);
      component.sessionForm?.controls['description'].setValue(updatedSession.description);
      fixture.detectChanges()

      const submitButton = fixture.debugElement.nativeElement.querySelector('button[type="submit"]');
      ngZone.run(() => {
        submitButton.click();
      });
      fixture.detectChanges();

      expect(mockSessionApiService.update).toHaveBeenCalledWith(
        sessionToEdit.id?.toString(), 
        {
          name: updatedSession.name,
          date: sessionToEdit.date.toISOString().split('T')[0],
          teacher_id: sessionToEdit.teacher_id,
          description: updatedSession.description,
        }
      );

      const snackBarContainer = document.querySelector('.mat-snack-bar-container');
      expect(snackBarContainer?.textContent).toContain('Session updated !');
    })
  })
});
