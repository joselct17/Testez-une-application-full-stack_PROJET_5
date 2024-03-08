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
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Router } from '@angular/router';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  
  // Connexion
  it('should login without error', () => {
    const authservice = TestBed.inject(AuthService);
    const login = jest.spyOn(authservice, 'login').mockImplementation(() => of({} as SessionInformation));
    const sessionService = jest.spyOn(TestBed.inject(SessionService), 'logIn');
    const route = jest.spyOn(TestBed.inject(Router), 'navigate').mockImplementation(async () => true);
    component.submit()
    expect(login).toHaveBeenCalled();
    expect(sessionService).toHaveBeenCalled();
    expect(route).toHaveBeenCalledWith((['/sessions']));
  });

  it('should not login if error', () => {
    const authservice = TestBed.inject(AuthService);
    const loginSpy = jest.spyOn(authservice, 'login').mockImplementation(() => throwError(() => new Error('err')));
    component.submit()
    expect(loginSpy).toHaveBeenCalled();
    expect(component.onError).toBe(true);
  });

  it('should display an error message when wrong login', () => {
    const authservice = TestBed.inject(AuthService);
    jest.spyOn(authservice, 'login').mockImplementation(() => throwError(() => new Error('err')));
    component.submit()
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('.error').textContent).toContain('An error occurred');
  });
});