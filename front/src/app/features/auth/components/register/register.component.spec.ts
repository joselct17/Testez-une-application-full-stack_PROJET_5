import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        RouterTestingModule,
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
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create the account', () => {
    const authservice = TestBed.inject(AuthService);
    const register = jest.spyOn(authservice, 'register').mockImplementation(() => of(undefined));
    const route = jest.spyOn(TestBed.inject(Router), 'navigate').mockImplementation(async () => true);
    component.submit()
    expect(register).toHaveBeenCalled();
    expect(route).toHaveBeenCalledWith((['/login']));
  });

  it('should not create the account if error', () => {
    const authservice = TestBed.inject(AuthService);
    jest.spyOn(authservice, 'register').mockImplementation(() => throwError(() => new Error('err')));
    component.submit()
    expect(component.onError).toBeTruthy();
  });
});
