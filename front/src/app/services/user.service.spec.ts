import { TestBed } from '@angular/core/testing';
import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let service: UserService;
  let httpController: HttpTestingController;

  const mockUser: User = {
    id: 1,
    email: 'email@exemple.fr',
    lastName: 'lastName',
    firstName: 'firstName',
    password: 'password',
    admin: false,
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
    });

    service = TestBed.inject(UserService);
    httpController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve user by ID', () => {
    service.getById('1').subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const req = httpController.expectOne({
      method: 'GET',
      url: `api/user/1`
    });

    req.flush(mockUser);
  });

  it('should delete user by ID', () => {
    service.delete('1').subscribe(response => {
      expect(response).toEqual({});
    });

    const req = httpController.expectOne({
      method: 'DELETE',
      url: `api/user/1`
    });

    req.flush({});
  });
});
