describe('Me spec', () => {
    it('Successfull get user informations', () => {
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

        cy.intercept(
            {
                method: 'GET',
                url: '/api/user/1',
            },
            {
                "id": 4,
                "email": "yoga@studio.com",
                "lastName": "Admin",
                "firstName": "Admin",
                "admin": true,
                "createdAt": "2024-03-29T17:11:33",
                "updatedAt": "2024-03-29T17:11:33"
            }
        )
        
  
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
        cy.url().should('include', '/sessions')

        cy.get('span').contains('Account').click()
    })
}); 