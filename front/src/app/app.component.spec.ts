import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { Router } from "@angular/router";
import { of } from "rxjs";
import {SessionService} from "./services/session.service";


describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let router: Router;

  const mockSessionService = {
      logOut: jest.fn().mockReturnValue(of()),
      $isLogged: jest.fn().mockReturnValue(of(true)),
      sessionInformation: {
        id: '1',
        name: 'name',
        email: 'email@test.fr'
      }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        RouterTestingModule,
        MatToolbarModule,
      ],
      providers: [{
        provide: SessionService,
        useValue: mockSessionService
      }],
      declarations: [
        AppComponent
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should check login state', () => {
    let state!: boolean;
    component.$isLogged().subscribe((data: boolean) => (state = data));
    expect(state).toBe(true);
  });

  it('should navigate to home when logOut', () => {
    let navigateSpy = jest.spyOn(router, 'navigate').mockImplementation();
    component.logout();
    expect(navigateSpy).toHaveBeenCalledWith(['']);
  });

  it('should return isLogged', () => {
    const isLoggedSpy = jest.spyOn(component, '$isLogged');
    const sessionServiceSpy = jest.spyOn(mockSessionService, '$isLogged').mockReturnValue(of(true));
    const isLogged = component.$isLogged();
    expect(isLoggedSpy).toHaveBeenCalled();
    expect(sessionServiceSpy).toHaveBeenCalled();
    expect(isLogged).toBeTruthy();
  });
});
