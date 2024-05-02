describe('Create session spec', () => {
  it('Successfull Create session', () => {
    cy.visit('/login')

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

    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          lastName: "DELAHAYE",
          firstName: "Margot",
          createdAt: "2024-02-23T18:23:27",
          updatedAt: "2024-02-23T18:23:27"
        },
        {
          id: 2,
          lastName: "THIERCELIN",
          firstName: "Hélène",
          createdAt: "2024-02-23T18:23:27",
          updatedAt: "2024-02-23T18:23:27"
        }
      ]
    })
    
    cy.intercept('POST', '/api/session', {
      body: {
        "name": "session 1",
        "date": "2012-01-01",
        "teacher_id": 5,
        "users": null,
        "description": "my description"
    }
    })


    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
    cy.get('button').click()
    cy.url().should('include', '/sessions/create')
    cy.get('input[formControlName=name]').type("Session name")
    cy.get('input[formControlName=date]').type("2024-09-09")
    cy.get('mat-select').click()
    cy.get('mat-option').first().click()
    cy.get('textarea').type("Session description")
    cy.get('button').contains('Save').click()
    cy.url().should('include', '/sessions')
  })
});