import { HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { Router } from '@angular/router';
import { throwError, of } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  const mockAuthService = {
    login: jest.fn()
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logIn: jest.fn(),
    logOut: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('form invalid when empty', () => {
    expect(component.form.valid).toBeFalsy();
  });

  it('should set onError to true on failed login attempt', fakeAsync(() => {
    let errorResponse = new HttpErrorResponse({
      error: 'test 404 error',
      status: 404, statusText: 'Not Found'
    });

    jest.spyOn(mockAuthService, 'login').mockReturnValue(throwError(() => errorResponse));

    component.submit();
    tick(); // simulate passage of time for the async operation to resolve
    expect(component.onError).toBe(true);
  }));

  it('should handle successful login', () => {
    const mockUser = {
      id: 1,
      username: 'test@test.fr',
      firstName: 'firstName',
      lastName: 'lastName',
      token: 'mockToken',
      type: 'Bearer',
      admin: false
    };

    jest.spyOn(mockAuthService, 'login').mockReturnValue(of(mockUser));
    jest.spyOn(mockSessionService, 'logIn');
    jest.spyOn(mockRouter, 'navigate');

    component.form.controls['email'].setValue('test@test.fr');
    component.form.controls['password'].setValue('password');
    component.submit();

    expect(mockAuthService.login).toHaveBeenCalled();
    expect(mockSessionService.logIn).toHaveBeenCalledWith(mockUser);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('form should be valid when all fields are filled', () => {
    const email = component.form.controls['email'];
    const password = component.form.controls['password'];

    email.setValue('test@example.com');
    password.setValue('123456');

    expect(component.form.valid).toBeTruthy();
  });

});
