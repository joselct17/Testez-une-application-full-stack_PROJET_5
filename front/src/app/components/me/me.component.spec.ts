import { HttpClientModule, HttpResponse } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { SessionService } from 'src/app/services/session.service';
import { User } from 'src/app/interfaces/user.interface';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let sessionService: SessionService;
  let router: Router;
  let matSnackBar: MatSnackBar;

  const user: User = {
    id: 1,
    email: "test@email.fr",
    lastName: "lastName",
    firstName: "firstName",
    admin: true,
    password: "password",
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockUserService = {
    getById: jest.fn().mockReturnValue(of(user)),
    delete: jest.fn().mockReturnValue(of(new HttpResponse({ status: 200, statusText: "OK" })))
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn(),
  };

  const mockSnackBar = {
    open: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        RouterTestingModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: UserService, useValue: mockUserService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: Router, useValue: mockRouter }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load user data on init', () => {
    component.ngOnInit();
    expect(userService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(user);
  });

  it('should delete the user account and navigate to home on delete', fakeAsync(() => {
    component.delete();
    tick();
    expect(userService.delete).toHaveBeenCalledWith('1');
    expect(matSnackBar.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    expect(sessionService.logOut).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/']);
  }));

  it('should call back on back button click', () => {
    const historySpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(historySpy).toHaveBeenCalled();
  });
});
