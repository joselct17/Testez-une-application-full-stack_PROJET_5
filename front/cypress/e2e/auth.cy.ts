describe('Register spec', () => {
  it('should register successfully', () => {
    // Visiter la page d'inscription
    cy.visit('/register')

    // Interception de la requête POST pour l'inscription
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200, // Assurer une réponse de succès
      body: {
        id: 1,
        username: 'username@username.com',
        firstName: 'firstName',
        lastName: 'lastName',
      },
    }).as('register')

    // Interception pour la session de l'utilisateur après l'inscription
    cy.intercept('GET', '/api/login', []).as('session')

    // Saisie des informations d'inscription
    cy.get('input[formControlName=firstName]').type('firstName')
    cy.get('input[formControlName=lastName]').type('lastName')
    cy.get('input[formControlName=email]').type('username@username.com')
    cy.get('input[formControlName=password]').type('password')

    // Soumission du formulaire
    cy.get('button[type=submit]').click()

    // Attendre que la requête d'inscription ait été effectuée
    cy.wait('@register').then((interception) => {
      expect(interception.response.statusCode).to.equal(200)
    })

    // Vérifier la redirection ou le message de succès après inscription
    cy.url().should('include', '/login') // Vérifie si la redirection vers /login a bien eu lieu
  })
})


describe('Register error spec', () => {
  it('should display an error message on registration failure', () => {
    // Visiter la page d'inscription
    cy.visit('/register')

    // Intercepter la requête d'inscription avec une réponse d'erreur
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400, // Simuler une erreur
      body: {
        message: 'Registration failed',
      },
    }).as('registerError')

    // Remplir le formulaire avec des données
    cy.get('input[formControlName=firstName]').type('firstName')
    cy.get('input[formControlName=lastName]').type('lastName')
    cy.get('input[formControlName=email]').type('username@username.com')
    cy.get('input[formControlName=password]').type('password')

    // Soumettre le formulaire
    cy.get('button[type=submit]').click()

    // Attendre que la requête d'inscription échoue
    cy.wait('@registerError')

    // Vérifier que le message d'erreur est affiché
    cy.get('.error').should('contain', 'An error occurred')
  })
})



