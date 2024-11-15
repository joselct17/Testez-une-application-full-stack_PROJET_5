import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {of} from "rxjs";

// Verify integration between LoginComponent, AuthService and SessionService
describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockAuthService: Partial<AuthService>;
  let mockSessionService: Partial<SessionService>;
  let router: Router

  beforeEach(async () => {
    // Mock the AuthService with a successful login response
    mockAuthService = {
      login: jest.fn().mockReturnValue(of({
        token: '1234',
        type: 'Bearer',
        id: 1,
        username: 'username',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false
      }))
    };

    // Mock the SessionService for handling session information
    mockSessionService = {
      logIn: jest.fn().mockReturnValue({})
    };

    // Configure the testing module
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        {provide: SessionService, useValue: mockSessionService},
        {provide: AuthService, useValue: mockAuthService},
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

    // Create the component instance and trigger change detection
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
  });

  // Verify that the component is created successfully
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not valid the form when password are empty', () => {
    const email = component.form.get('email');
    const password = component.form.get('password');

    if (email && password) {
      email.setValue('example@example.com');
      password.setValue('');

      expect(component.form.valid).toBe(false);
    }
  });
});
