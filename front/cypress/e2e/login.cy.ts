describe('Login spec', () => {
  it('Login successfull', () => {
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

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })
});


describe('Login spec', () => {
  it('should display an error message on login failure', () => {
    // Visiter la page de connexion
    cy.visit('/login')

    // Intercepter la requête de connexion avec une réponse d'erreur
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401, // Simuler une erreur d'authentification
      body: {
        message: 'Invalid credentials',
      },
    }).as('loginError')

    // Remplir le formulaire de connexion avec des informations d'identification incorrectes
    cy.get('input[formControlName=email]').type('invalid@user.com')
    cy.get('input[formControlName=password]').type('wrongpassword')

    // Soumettre le formulaire
    cy.get('button[type=submit]').click()

    // Attendre que la requête de connexion échoue
    cy.wait('@loginError')

    // Vérifier que le message d'erreur est affiché
    cy.get('.error').should('contain', 'An error occurred')
  })
});



