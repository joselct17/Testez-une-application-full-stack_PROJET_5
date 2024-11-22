import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { Session } from '../../interfaces/session.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { TeacherService } from 'src/app/services/teacher.service';
import { By } from '@angular/platform-browser';

describe('DetailComponent', () => { // Déclaration de la suite de tests pour DetailComponent
  let component: DetailComponent; // Variable pour contenir une instance du composant
  let fixture: ComponentFixture<DetailComponent>; // Fixture pour interagir avec le composant et son DOM
  let mockSessionService: Partial<SessionService>; // Mock du service de gestion des sessions
  let mockSessionApiService: Partial<SessionApiService>; // Mock du service API des sessions
  let mockTeacherService: Partial<TeacherService>; // Mock du service de gestion des enseignants
  let spyHistoryBack: jest.SpyInstance; // Espion pour surveiller l'utilisation de window.history.back
  let matSnackBarMock: Partial<MatSnackBar>; // Mock pour le MatSnackBar
  let router: Router; // Router Angular pour les tests d'intégration


  beforeEach(async () => { // Configuration initiale avant chaque test
    mockSessionService = { // Simulation d'une session utilisateur
      sessionInformation: {
        token: '1234',
        type: 'Bearer',
        id: 1,
        username: 'username',
        firstName: 'user',
        lastName: 'name',
        admin: false // L'utilisateur n'est pas administrateur
      }
    };

    matSnackBarMock = { // Simulation d'un MatSnackBar
      open: jest.fn() // Mock de la méthode `open` pour afficher des messages
    };
    mockSessionApiService = { // Simulation des appels API liés aux sessions
      detail: jest.fn().mockReturnValue(of({ // Mock de l'API de détail de session
        id: 1,
        name: 'Test Session',
        users: [123], // Liste des utilisateurs participants
        teacher_id: 456,
        description: 'desc',
        date: new Date("2024-08-22T10:32:09.475Z"),
        createdAt: new Date("2024-08-21T10:32:09.475Z"),
        updatedAt: new Date("2024-08-23T10:32:09.475Z")
      })),
      delete: jest.fn().mockReturnValue(of({})), // Mock pour la suppression
      participate: jest.fn().mockReturnValue(of({})), // Mock pour participer
      unParticipate: jest.fn().mockReturnValue(of({})) // Mock pour se désinscrire
    };

    mockTeacherService = { // Simulation des appels API liés aux enseignants
      detail: jest.fn().mockReturnValue(of({
        id: 1,
        lastName: 'lastName',
        firstName: 'firstName',
        createdAt: new Date("2024-08-22T10:32:09.475Z"),
        updatedAt: new Date("2024-08-22T10:32:09.475Z")
      }))
    };

    await TestBed.configureTestingModule({ // Configuration du module de test
      imports: [
        RouterTestingModule, // Mock du routing Angular
        HttpClientModule, // Import pour les appels HTTP
        MatSnackBarModule, // Import du MatSnackBar
        ReactiveFormsModule, // Module pour les formulaires réactifs
        MatButtonModule, MatCardModule, MatIconModule // Modules Angular Material
      ],
      declarations: [DetailComponent], // Déclaration du composant testé
      providers: [ // Fourniture des mocks pour les services
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        { provide: TeacherService, useValue: mockTeacherService },
      ],
    }).compileComponents(); // Compilation du composant et de ses dépendances

    spyHistoryBack = jest.spyOn(window.history, 'back'); // Espionne l'appel à history.back
    fixture = TestBed.createComponent(DetailComponent); // Création d'une instance du composant
    component = fixture.componentInstance; // Récupération de l'instance
    fixture.detectChanges(); // Détection des changements dans le DOM
    router = TestBed.inject(Router); // Injection du router Angular
  });

  afterEach(() => {
    spyHistoryBack.mockRestore(); // Restaure l'état initial de l'espion après chaque test
  });



  it('should go back to the previous page from history', () => {
    component.back(); // Appel de la méthode `back` du composant
    expect(spyHistoryBack).toHaveBeenCalled(); // Vérifie que `history.back` a été appelé
  });


  it('should call sessionApiService.delete and navigate to sessions after deletion', () => {
    jest.spyOn(router, 'navigate'); // Espionne la navigation du router
    component.delete(); // Appelle la méthode `delete` du composant
    expect(mockSessionApiService.delete).toHaveBeenCalledWith(component.sessionId); // Vérifie l'appel à l'API
    expect(matSnackBarMock.open).toHaveBeenCalledWith( // Vérifie que le snack-bar affiche un message
      'Session deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(router.navigate).toHaveBeenCalledWith(['sessions']); // Vérifie la navigation vers `/sessions`
  });

  it('should call sessionApiService.participate and refresh session details', () => {
    // Fake values
    component.sessionId = 'mock-session-id';
    component.userId = 'mock-user-id';

    const mockTeacher: Teacher = {
      id: 1,
      lastName: 'lastName',
      firstName: 'firstName',
      createdAt: new Date("2024-08-22T10:32:09.475Z"),
      updatedAt: new Date("2024-08-22T10:32:09.475Z")
    }

    const mockSession: Session = {
      id: 1,
      name: 'Test Session',
      users: [123],
      teacher_id: 456,
      description: 'desc',
      date: new Date("2024-08-22T10:32:09.475Z"),
      createdAt: new Date("2024-08-21T10:32:09.475Z"),
      updatedAt: new Date("2024-08-23T10:32:09.475Z")
    }

    const participateSpy = jest.spyOn(mockSessionApiService, 'participate').mockReturnValue(of())

    component.participate()
    expect(participateSpy).toHaveBeenCalledWith(component.sessionId, component.userId)

    fixture.detectChanges()
    expect(component.session).toEqual(mockSession)
    expect(component.teacher).toEqual(mockTeacher)
  })

  it('should call sessionApiService.unparticipate and refresh session details', () => {
    component.sessionId = 'mock-session-id';
    component.userId = 'mock-user-id';

    const mockTeacher: Teacher = {
      id: 1,
      lastName: 'lastName',
      firstName: 'firstName',
      createdAt: new Date("2024-08-22T10:32:09.475Z"),
      updatedAt: new Date("2024-08-22T10:32:09.475Z")
    }

    const mockSession: Session = {
      id: 1,
      name: 'Test Session',
      users: [123],
      teacher_id: 456,
      description: 'desc',
      date: new Date("2024-08-22T10:32:09.475Z"),
      createdAt: new Date("2024-08-21T10:32:09.475Z"),
      updatedAt: new Date("2024-08-23T10:32:09.475Z")
    }

    const unParticipateSpy = jest.spyOn(mockSessionApiService, 'unParticipate').mockReturnValue(of())

    component.unParticipate()
    expect(unParticipateSpy).toHaveBeenCalledWith(component.sessionId, component.userId)

    fixture.detectChanges()
    expect(component.session).toEqual(mockSession)
    expect(component.teacher).toEqual(mockTeacher)

  })

  it('should display delete button if user is admin', () => {
    component.isAdmin = true
    const deleteButton = fixture.debugElement.query(By.css(`button[ng-reflect-ng-click="delete()"]`))
    expect(deleteButton).toBeTruthy
  })

  it('should display sessions dom elements correctly', () => {
    const sessionName = fixture.debugElement.query(By.css('h1'))
    const spans = fixture.debugElement.query(By.css('.my2')).nativeElement.querySelectorAll('span')
    const sessionUsers = spans[0];
    const sessionDate = spans[1];
    const sessionDescription = fixture.debugElement.query(By.css('.description'))
    const sessionCreated = fixture.debugElement.query(By.css('.created'))
    const sessionUpdated = fixture.debugElement.query(By.css('.updated'))

    expect(sessionName.nativeElement.textContent).toContain('Test Session')
    expect(sessionUsers.textContent).toContain('1 attendees')
    expect(sessionDate.textContent).toContain('August 22, 2024')
    expect(sessionDescription.nativeElement.textContent).toContain('desc')
    expect(sessionCreated.nativeElement.textContent).toContain('Create at:  August 21, 2024')
    expect(sessionUpdated.nativeElement.textContent).toContain('Last update:  August 23, 2024')
  })
});
