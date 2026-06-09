describe('Smoke test', () => {
  it('loads the application', () => {
    cy.visit('/');
    cy.contains('Haru Shop').should('be.visible');
    cy.contains('Dashboard').should('be.visible');
  });
});
