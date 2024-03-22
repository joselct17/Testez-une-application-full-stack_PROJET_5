import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { DetailComponent } from './detail.component';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { Session } from '../../interfaces/session.interface';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [{ provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ]
      
    })
      .compileComponents();
      service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should go back', () => {
    const back = jest.spyOn(window.history, 'back');
    component.back();
    expect(back).toHaveBeenCalled();
  });

  it('should delete the session', () => {
    const sessionApiService = TestBed.inject(SessionApiService);
    const deleteSession = jest.spyOn(sessionApiService, 'delete').mockReturnValue(of({}));
    const matSnackBar = jest.spyOn(TestBed.inject(MatSnackBar), 'open').mockImplementation();
    const route = jest.spyOn(TestBed.inject(Router), 'navigate').mockImplementation(async () => true);
    component.delete();
    expect(deleteSession).toHaveBeenCalled();
    expect(matSnackBar).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(route).toHaveBeenCalledWith((['sessions']));
  });

  it('should participate in the session', () => {
    const sessionApiService = TestBed.inject(SessionApiService);
    const participate = jest.spyOn(sessionApiService, 'participate').mockReturnValue(of(undefined));
    component.participate();
    expect(participate).toHaveBeenCalled();
  });

  it('should unparticipate in the session', () => {
    const sessionApiService = TestBed.inject(SessionApiService);
    const unParticipate = jest.spyOn(sessionApiService, 'unParticipate').mockReturnValue(of(undefined));
    component.unParticipate();
    expect(unParticipate).toHaveBeenCalled();
  });

  it('should fetch the session', () => {
    const sessionApiService = TestBed.inject(SessionApiService);
    const detail = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of({} as Session));
    component.ngOnInit();
    expect(detail).toHaveBeenCalled();
  });
});

