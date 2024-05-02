describe('Login spec', () => {
    it('Login successfull', () => {
        cy.visit('/login')

        cy.intercept('POST', '/api/auth/login', {
        body: {
            id: 3,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: false
        },
        })

        cy.intercept(
        {
            method: 'GET',
            url: '/api/session',
        },
        [
            {
                "id": 1,
                "name": "test",
                "date": "2024-02-10T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "test",
                "users": [],
                "createdAt": "2024-02-23T19:27:37",
                "updatedAt": "2024-02-23T19:27:37"
            }
        ]).as('session')

        // get session 1
        cy.intercept(
            {
                method: 'GET',
                url: '/api/session/1',
            },
            {
                "id": 1,
                "name": "test",
                "date": "2024-02-10T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "test",
                "users": [],
                "createdAt": "2024-02-23T19:27:37",
                "updatedAt": "2024-03-29T18:14:48"
            }
        )

        cy.intercept(
            {
                method: 'GET',
                url: '/api/teacher/1',
            },
            {
                "id": 1,
                "lastName": "DELAHAYE",
                "firstName": "Margot",
                "createdAt": "2024-02-23T18:23:27",
                "updatedAt": "2024-02-23T18:23:27"
            }
        )

        cy.intercept('POST', '/api/session/1/participate/3', [])

        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

        cy.url().should('include', '/sessions')

        cy.get('button').contains('Detail').click()

        cy.url().should('include', '/sessions/detail')
        
        cy.intercept(
            {
                method: 'GET',
                url: '/api/session/1',
            },
            {
                "id": 1,
                "name": "test",
                "date": "2024-02-10T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "test",
                "users": [
                    3
                ],
                "createdAt": "2024-02-23T19:27:37",
                "updatedAt": "2024-03-29T18:14:48"
            }
        )

        cy.get('button').contains('Participate').click()

        

    })
});