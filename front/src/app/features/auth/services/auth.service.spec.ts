import { TestBed } from "@angular/core/testing";
import { AuthService } from "./auth.service";
import { expect } from '@jest/globals';
import { HttpClientModule } from "@angular/common/http";
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";

describe('AuthService', () => {
    let service: AuthService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports:[
              HttpClientModule,
              HttpClientTestingModule
            ]
          });
        service = TestBed.inject(AuthService);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });

    it('should register', () => {
        const registerRequest = {
            email: 'test@gmail.com',
            password: 'test1234',
            firstName: 'test',
            lastName: 'test'
        };
        const observable = service.register(registerRequest);
        let registerSubscribe;
        observable.subscribe(register => {
            registerSubscribe = register;
        });
        expect(observable).toBeTruthy();
        const httpMock = TestBed.inject(HttpTestingController);
        const req = httpMock.expectOne('api/auth/register');
        expect(req.request.method).toBe('POST');
        req.flush(registerRequest);
        expect(registerSubscribe).toEqual(registerRequest);
    });

    it('should login', () => {
        const loginRequest = {
            email: '',
            password: ''
        };
        const observable = service.login(loginRequest);
        let loginSubscribe;
        observable.subscribe(login => {
            loginSubscribe = login;
        });
        expect(observable).toBeTruthy();
        const httpMock = TestBed.inject(HttpTestingController);
        const req = httpMock.expectOne('api/auth/login');
        expect(req.request.method).toBe('POST');
        req.flush(loginRequest);
        expect(loginSubscribe).toEqual(loginRequest);
    });
});