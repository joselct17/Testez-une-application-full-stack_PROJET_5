import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { of } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: { register: jest.fn() } },
        { provide: Router, useValue: { navigate: jest.fn() } }
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should have a form invalid when empty', () => {
    expect(component.form.valid).toBeFalsy();
  });

  it('should enable the submit button when the form is valid', () => {
    let password = component.form.controls['password'];
    let email = component.form.controls['email'];
    let firstName = component.form.controls['firstName'];
    let lastName = component.form.controls['lastName'];
    let submit = fixture.nativeElement.querySelector('button[type]');

    password.setValue("password");
    email.setValue("email@test.fr");
    firstName.setValue("firstName");
    lastName.setValue("lastName");
    fixture.detectChanges();
    expect(component.form.valid).toBeTruthy();
    expect(submit.disabled).toEqual(false);
  });

  it('should disable the submit button when the form is invalid', () => {
    component.form.controls['email'].setValue('');
    component.form.controls['password'].setValue('');
    component.form.controls['firstName'].setValue('');
    component.form.controls['lastName'].setValue('');
    fixture.detectChanges();
    let submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(submitButton.disabled).toBeTruthy();
  });

  it('should redirect to login page when submit', () => {
    const navigateSpy = jest.spyOn(router, 'navigate').mockImplementation(async () => true);
    const authSpy = jest.spyOn(authService, 'register').mockImplementation(() => of(undefined));
    component.submit();
    expect(authSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
  });

  it('should show an error when error', () => {
    component.onError = true;
    fixture.detectChanges();
    const formElement: HTMLElement = fixture.nativeElement;
    const errorMessage = formElement.querySelector('span.error');
    expect(errorMessage).toBeTruthy();
    expect(errorMessage!.textContent).toContain('An error occurred');
  });
});
