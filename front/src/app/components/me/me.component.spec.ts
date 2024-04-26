import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MeComponent } from './me.component';
import { UserService } from '../../services/user.service';
import { SessionService } from 'src/app/services/session.service';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let sessionService: SessionService;
  let matSnackBar: MatSnackBar;
  let router: Router;

  const mockMatSnackBar = {
    open: jest.fn()
  }

  beforeEach(async () => {
    const sessionServiceMock = {
      sessionInformation: {
        id: 1
      },
      logOut: jest.fn()
    };

    const userServiceMock = {
      getById: jest.fn().mockReturnValue(of({
        id: 1,
        email: 'test@exemple.fr',
        lastName: 'Nom',
        firstName: 'Prenom',
        admin: true,
        password: 'password',
        createdAt: new Date()
      })),
      delete: jest.fn().mockReturnValue(of({}))
    };

    await TestBed.configureTestingModule({
      declarations: [ MeComponent ],
      imports: [
        HttpClientTestingModule,
        MatSnackBarModule,
        RouterTestingModule,
        MatCardModule,
        MatIconModule,
        MatInputModule,
        MatFormFieldModule
      ],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load user data on init', () => {
    component.ngOnInit();
    expect(userService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual({
      id: 1,
      email: 'test@exemple.fr',
      lastName: 'Nom',
      firstName: 'Prenom',
      admin: true,
      password: 'password',
      createdAt: expect.any(Date)
    });
  });

  it('should navigate back on back call', () => {
    const spy = jest.spyOn(window.history, 'back');
    component.back();
    expect(spy).toHaveBeenCalled();
  });

  it('should delete the user account and navigate to home on delete', () => {
    const navigateSpy = jest.spyOn(router, 'navigate');
    component.delete();
    expect(userService.delete).toHaveBeenCalledWith('1');
    expect(mockMatSnackBar.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    expect(sessionService.logOut).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/']);
  });
});
