import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { Router } from '@angular/router';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  } 

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
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create the session', () => {
    const sessionApiService = TestBed.inject(SessionApiService);
    const create = jest.spyOn(sessionApiService, 'create').mockImplementation(() => of({} as Session));
    const matSnackBar = jest.spyOn(TestBed.inject(MatSnackBar), 'open').mockImplementation();
    const route = jest.spyOn(TestBed.inject(Router), 'navigate').mockImplementation(async () => true);
    component.submit()
    expect(create).toHaveBeenCalled();
    expect(matSnackBar).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 })
    expect(route).toHaveBeenCalledWith((['sessions']));
  });

  it('should update the session', () => {
    const sessionApiService = TestBed.inject(SessionApiService);
    const update = jest.spyOn(sessionApiService, 'update').mockImplementation(() => of({} as Session));
    const matSnackBar = jest.spyOn(TestBed.inject(MatSnackBar), 'open').mockImplementation();
    const route = jest.spyOn(TestBed.inject(Router), 'navigate').mockImplementation(async () => true);
    component.onUpdate = true;
    component.submit()
    expect(update).toHaveBeenCalled();
    expect(matSnackBar).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 })
    expect(route).toHaveBeenCalledWith((['sessions']));
  });
});
