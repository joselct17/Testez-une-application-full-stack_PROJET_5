// ***********************************************
// This example namespace declaration will help
// with Intellisense and code completion in your
// IDE or Text Editor.
// ***********************************************
// declare namespace Cypress {
//   interface Chainable<Subject = any> {
//     customCommand(param: any): typeof customCommand;
//   }
// }
//
// function customCommand(param: any): void {
//   console.warn(param);
// }
//
// NOTE: You can use it like so:
// Cypress.Commands.add('customCommand', customCommand);
//
// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })

Cypress.Commands.add('login', (isAdmin = false) => {
  cy.intercept('POST', '/api/auth/login', {
    body: {
      id: 1,
      username: isAdmin ? 'admin@example.com' : 'user@example.com',
      firstName: isAdmin ? 'AdminFirst' : 'UserFirst',
      lastName: isAdmin ? 'AdminLast' : 'UserLast',
      admin: isAdmin,
    },
  }).as('login');

  cy.visit('/login');

  cy.get('input[formControlName=email]').type(isAdmin ? 'admin@studio.com' : 'yoga@studio.com');
  cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

  // Attendre que la requête soit bien interceptée
  cy.wait('@login');
});



