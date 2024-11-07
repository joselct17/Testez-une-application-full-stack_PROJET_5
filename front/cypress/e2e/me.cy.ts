describe('User Information', () => {
  beforeEach(() => {
    cy.login();
  });

  it('should display user information correctly', () => {
    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        firstName: 'firstName',
        lastName: 'lastName',
        email: 'yoga@studio.com',
        admin: true,
        createdAt: ' May 29, 2024',
        updatedAt: 'May 29, 2024',
      }
    }).as('getUserInfo');

    cy.get('span.link').contains('Account').click();
    cy.wait('@getUserInfo');

    cy.get('p').contains('Name: firstName LASTNAME');
    cy.get('p').contains('Email: yoga@studio.com');
    cy.get('p').contains('Create at: May 29, 2024');
    cy.get('p').contains('Last update: May 29, 2024');
  });

  it('should delete user account successfully', () => {
    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        firstName: 'firstName',
        lastName: 'lastName',
        email: 'user@example.com',
        admin: false,
        createdAt: '2024-09-20T10:24:58',
        updatedAt: '2024-09-21T12:12:05',
      }
    }).as('getUserInfo');

    cy.intercept('DELETE', '/api/user/1', {
      statusCode: 200,
      body: {}
    }).as('deleteUser');

    cy.get('span.link').contains('Account').click();
    cy.wait('@getUserInfo');

    cy.get('button').contains('Detail').click();
    cy.wait('@deleteUser');

    cy.url().should('include', '/');
    cy.get('.mat-snack-bar-container').should('contain', 'Your account has been deleted !');
  });
});
