import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { of } from 'rxjs';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { User } from 'src/app/interfaces/user.interface';
import { formatDate, registerLocaleData } from '@angular/common';
import localeEn from '@angular/common/locales/en';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  let mockSessionService: Partial<SessionService>
  let mockUserService: Partial<UserService>
  let router: Partial<Router>
  let matSnackBarMock: Partial<MatSnackBar>
  const mockUser: User = {
    id: 1,
    email: 'string',
    lastName: 'string',
    firstName: 'string',
    admin: true,
    password: "string",
    createdAt: new Date(),
    updatedAt: new Date()
  };
  let spyHistoryBack: jest.SpyInstance

  beforeEach(async () => {
    matSnackBarMock = {
      open: jest.fn()
    }
    mockSessionService = {
      sessionInformation: {
        token: '1234',
        type: 'Bearer',
        id: 1,
        username: 'username',
        firstName: 'user',
        lastName: 'name',
        admin: false
      },
      logOut: jest.fn()
    }
    mockUserService = {
      getById: jest.fn().mockReturnValue(of(mockUser)),
      delete: jest.fn().mockReturnValue(of(null))
    };

    // Create a mock for Router
    router = {
      navigate: jest.fn()
    }

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
      providers: [
        {provide: SessionService, useValue: mockSessionService},
        {provide: UserService, useValue: mockUserService},
        {provide: MatSnackBar, useValue: matSnackBarMock},
        {provide: Router, useValue: router}
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    spyHistoryBack = jest.spyOn(window.history, 'back');
  });

  // Cleanup after each test
  afterEach(() => {
    spyHistoryBack.mockRestore();
  });

  // Verify that the component is created successfully
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test the back method, which navigates to the previous page
  it('should go back to the previous page', () => {
    component.back()
    expect(spyHistoryBack).toHaveBeenCalled()
  })

  it('should display user information', () => {
    const user = {
      id: 1,
      email: 'test@mail.com',
      lastName: 'test',
      firstName: 'test',
      admin: true,
      password: 'test',
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    component.user = user;
    fixture.detectChanges();
    const compiled = fixture.nativeElement;

    // Format dates to longDate
    registerLocaleData(localeEn);
    const locale = 'en-US';
    const createdAtFormatted = formatDate(user.createdAt, 'longDate', locale);
    const updatedAtFormatted = formatDate(user.updatedAt, 'longDate', locale);

    expect(compiled.textContent).toContain(user.lastName);
    expect(compiled.textContent).toContain(user.firstName);
    expect(compiled.textContent).toContain(user.email);
    expect(compiled.textContent).toContain(createdAtFormatted);
    expect(compiled.textContent).toContain(updatedAtFormatted);
  });
});
