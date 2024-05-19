import {ComponentFixture, TestBed} from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { ListComponent } from './list.component';
import { SessionApiService } from '../../services/session-api.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';
import {of} from "rxjs";

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  let mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockSessionApiService = {
    all: jest.fn().mockReturnValue(of([])),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        MatCardModule,
        MatIconModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize sessions$ correctly', () => {
    expect(component.sessions$).toBeTruthy();
    expect(mockSessionApiService.all).toHaveBeenCalled();
  });

  it('should display admin options for admin user', () => {
    const createButton = fixture.debugElement.query(By.css('.create-button'));
    const editButtons = fixture.debugElement.queryAll(By.css('.edit-button'));

    expect(createButton).toBeDefined();
    expect(editButtons).toBeDefined();
  });

  it('should not display admin options for non-admin user', () => {
    mockSessionService.sessionInformation.admin = false;
    fixture.detectChanges();

    const createButton = fixture.debugElement.query(By.css('.create-button'));
    const editButtons = fixture.debugElement.queryAll(By.css('.edit-button'));

    expect(createButton).toBeNull();
    expect(editButtons.length).toBe(0);
  });
});
