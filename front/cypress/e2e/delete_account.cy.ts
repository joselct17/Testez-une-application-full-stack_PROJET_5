describe('delete account spec', () => {
    it('Successfull delete my account', () => {
        cy.visit('/login')
  
        cy.intercept('POST', '/api/auth/login', {
            body: {
            id: 3,
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

        cy.intercept(
            {
                method: 'GET',
                url: '/api/user/3',
            },
            {
                "id": 3,
                "email": "toto3@toto.com",
                "lastName": "toto",
                "firstName": "toto",
                "admin": false,
                "createdAt": "2024-03-29T17:09:56",
                "updatedAt": "2024-03-29T17:09:56"
            }
        )

        cy.intercept('DELETE', '/api/user/3', [{}])
        
  
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
        cy.url().should('include', '/sessions')

        cy.get('span').contains('Account').click()

        cy.get('button').contains('Detail').click()

        cy.url().should('include', '/')
    })
}); 