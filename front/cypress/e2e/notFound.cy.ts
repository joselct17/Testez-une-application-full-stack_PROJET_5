describe('Not Found', () => {
    it('should display not found page', () => {
        cy.visit('/not-found');

        cy.url().should('include', '/404');
        cy.contains('Page not found !');
    });
});
