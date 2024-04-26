import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { Router } from '@angular/router';
import {SessionInformation} from "../../../../interfaces/sessionInformation.interface";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(async () => {
    const authServiceMock = {
      login: jest.fn()
    };

    const sessionServiceMock = {
      logIn: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        FormBuilder,
        { provide: AuthService, useValue: authServiceMock },
        { provide: SessionService, useValue: sessionServiceMock }
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
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call AuthService login on form submit and navigate to sessions', () => {
    const loginResponse: SessionInformation = {
      token: '123',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };
    jest.spyOn(authService, 'login').mockReturnValue(of(loginResponse));
    const navigateSpy = jest.spyOn(router, 'navigate');

    component.form.setValue({
      email: 'test@example.fr',
      password: 'password'
    });
    component.submit();

    expect(authService.login).toHaveBeenCalledWith({
      email: 'test@example.fr',
      password: 'password'
    });
    expect(sessionService.logIn).toHaveBeenCalledWith(loginResponse);
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBe(false);
  });

  it('should set onError to true if login fails', () => {
    jest.spyOn(authService, 'login').mockReturnValue(throwError(() => new Error('Login failed')));
    component.form.setValue({
      email: 'test@example.fr',
      password: 'password'
    });
    component.submit();

    expect(component.onError).toBe(true);
  });

  it('should have the correct initial state', () => {
    expect(component.hide).toBe(true);
    expect(component.onError).toBe(false);
    expect(component.form.valid).toBeFalsy();
  });

  it('should display error message when form is invalid', () => {
    const emailField = component.form.controls['email'];
    const passwordField = component.form.controls['password'];

    emailField.setValue('');
    passwordField.setValue('');
    fixture.detectChanges();

    expect(component.form.valid).toBeFalsy();
    expect(emailField.errors).not.toBeNull();
    expect(passwordField.errors).not.toBeNull();
  });

  it('should not submit the form if it is invalid', () => {
    jest.spyOn(authService, 'login').mockReturnValue(of({
      token: '',
      type: '',
      id: 0,
      username: '',
      firstName: '',
      lastName: '',
      admin: false
    } as SessionInformation));

    component.form.setValue({
      email: '',
      password: ''
    });

    if (component.form.valid) {
      component.submit();
    }

    expect(authService.login).not.toHaveBeenCalled();
    expect(component.onError).toBe(false);
  });

});
