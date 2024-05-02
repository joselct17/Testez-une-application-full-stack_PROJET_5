import { SessionService } from './../../services/session.service';
import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import { SessionApiService } from 'src/app/features/sessions/services/session-api.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/interfaces/user.interface';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: () => {}
  }
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
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

  it('should delete the user', () => {
    const sessionUserservice = TestBed.inject(UserService);
    const sessionService = TestBed.inject(SessionService);
    const deleteSession = jest.spyOn(sessionUserservice, 'delete').mockImplementation(() => of({}));
    const matSnackBar = jest.spyOn(TestBed.inject(MatSnackBar), 'open').mockImplementation();
    const route = jest.spyOn(TestBed.inject(Router), 'navigate').mockImplementation(async () => true);
    const logOut = jest.spyOn(sessionService, 'logOut'); 
    component.delete();
    expect(deleteSession).toHaveBeenCalled();
    expect(matSnackBar).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    expect(logOut).toHaveBeenCalled();
    expect(route).toHaveBeenCalledWith(['/']);
  });

  it('should get user by id', () => {
    const userService = TestBed.inject(UserService);
    const user : User = 
      {
        id: 1,
        email: '1',
        lastName: '1',
        firstName: '1',
        admin: true,
        password: '1',
        createdAt: new Date(),
        updatedAt: new Date()
      };
    const getById = jest.spyOn(userService, 'getById').mockImplementation(() => of(user));
    component.ngOnInit();
    expect(getById).toHaveBeenCalled();
    expect(component.user).toEqual(user);
  });
});
