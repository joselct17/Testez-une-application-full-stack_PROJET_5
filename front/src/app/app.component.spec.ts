import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { Router } from '@angular/router';
import { SessionService } from './services/session.service';
import { of } from 'rxjs';


describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should log out', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    const sessionService = TestBed.inject(SessionService);
    const router = TestBed.inject(Router);
    const logOut = jest.spyOn(sessionService, 'logOut').mockReturnValue();
    const navigate = jest.spyOn(router, 'navigate').mockImplementation(async () => true);
    app.logout();
    expect(logOut).toHaveBeenCalled();
    expect(navigate).toHaveBeenCalled();
  });

  it('should check if user is logged in', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    const sessionService = TestBed.inject(SessionService);
    const isLogged = jest.spyOn(sessionService, '$isLogged').mockImplementation(() => of(true));
    app.$isLogged();
    expect(isLogged).toHaveBeenCalled();
  });
});
