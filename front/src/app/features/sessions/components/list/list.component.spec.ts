import { of } from 'rxjs';
import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let session: Session = {
    name: 'test',
    description: 'test',
    date: new Date(),
    teacher_id: 1,
    users: []
  }

  const mockSessionService = {
    sessionInformation: {
      admin: true
    },
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule],
      providers: [{ provide: SessionService, useValue: mockSessionService }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the list of sessions', () => {
    const sessionApiService = TestBed.inject(SessionApiService);
    jest.spyOn(sessionApiService, 'all').mockReturnValue(of([session]));
    component.sessions$.subscribe((sessions) => {
      expect(sessions).toBeGreaterThan(0);
    });
  });

  it('should display Create and Detail buttons if the user is admin', () => {
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('.ml1').textContent).toContain('Create');
  });

  it('should get the user', () => {
    expect(component.user).toEqual(mockSessionService.sessionInformation);
  });
});
