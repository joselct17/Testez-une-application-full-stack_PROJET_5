import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    const authServiceMock = {
      register: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        FormBuilder,
        { provide: AuthService, useValue: authServiceMock }
      ],
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatInputModule

      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('form invalid when empty', () => {
    expect(component.form.valid).toBeFalsy();
  });

  it('email field validity', () => {
    let email = component.form.controls['email'];
    expect(email.valid).toBeFalsy();

    email.setValue('');
    expect(email.hasError('required')).toBeTruthy();

    email.setValue('test');
    expect(email.hasError('email')).toBeTruthy();
  });

  it('submitting a form emits a user', () => {
    fixture.detectChanges();
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['firstName'].setValue('John');
    component.form.controls['lastName'].setValue('Doe');
    component.form.controls['password'].setValue('validPassword123');
    expect(component.form.valid).toBeTruthy();

    const authServiceSpy = jest.spyOn(authService, 'register').mockReturnValue(of(undefined));
    const routerSpy = jest.spyOn(router, 'navigate');

    component.submit();

    expect(authServiceSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith(['/login']);
  });

  it('should handle register error', () => {
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['firstName'].setValue('John');
    component.form.controls['lastName'].setValue('Doe');
    component.form.controls['password'].setValue('12345');

    jest.spyOn(authService, 'register').mockReturnValue(throwError(() => new Error('Registration failed')));

    component.submit();

    expect(component.onError).toBe(true);
  });

  it('should not navigate to login if registration fails', () => {
    jest.spyOn(authService, 'register').mockReturnValue(throwError(() => new Error('Registration failed')));
    const navigateSpy = jest.spyOn(router, 'navigate');

    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['firstName'].setValue('John');
    component.form.controls['lastName'].setValue('Doe');
    component.form.controls['password'].setValue('12345');
    component.submit();

    expect(navigateSpy).not.toHaveBeenCalledWith(['/login']);
  });

  it('should display an error message when onError is true', () => {
    component.onError = true;
    fixture.detectChanges();

    const errorElement = fixture.nativeElement.querySelector('.error-message');
    expect(errorElement).toBeDefined();
  });

  it('should not allow submission with password less than 3 characters', () => {
    jest.spyOn(authService, 'register').mockReturnValue(of(undefined));

    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['firstName'].setValue('John');
    component.form.controls['lastName'].setValue('Doe');
    component.form.controls['password'].setValue('12');
    component.submit();

    expect(component.form.valid).toBeFalsy();
    expect(authService.register).not.toHaveBeenCalled();
  });

});
