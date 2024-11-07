// cypress/integration/homepage.cy.js

describe('Page d\'accueil', () => {
  it('devrait afficher le titre de l\'application', () => {
    cy.visit('/');
    cy.contains( 'Yoga app').should('be.visible');
  });
});
