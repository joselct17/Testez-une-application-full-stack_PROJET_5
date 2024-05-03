import * as cypress from "cypress";

describe('Login Component', () => {

  beforeEach(() => {
    cy.visit('login');
  });

  afterEach(() => {});

  it('should display the Login form', () => {
    cy.get('.login').should('exist');
    cy.get('mat-card-title').should('contain', 'Login');
    cy.get('.login-form').should('exist');
  });

  it('should display all input fields', () => {
    cy.get('input[formControlName=email]').should('exist');
    cy.get('input[formControlName=password]').should('exist');
  });

  it('should disabled button when email is empty', () => {
    cy.get('input[formControlName=email]').type("{enter}");
    cy.get('button').should('be.disabled');
  });

  it('should log in successfully', () => {

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
    cy.get('.error').should('not.exist')
  })

  it('should log out user after log in', () => {

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
    cy.contains('Logout').click();
    cy.url().should('include', '/');
    cy.contains('Login');
    cy.contains('Register');
    cy.contains('Logout').should('not.exist');
  });

  it('should display an error with bad credential and not log in', () => {

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        "path": "/api/auth/login",
        "error": "Unauthorized",
        "message": "Bad credentials",
        "status": 401
      }
    });

    cy.get('input[formControlName=email]').type("yoga@test.fr");
    cy.get('input[formControlName=password]').type(`${"1234"}{enter}{enter}`);

    cy.contains('An error occurred');
  });

});
