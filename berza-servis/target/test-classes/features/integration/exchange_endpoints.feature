Feature: ExchangeController API Endpoints

  Scenario: Get exchange by name
    When I search for exchange by name "Nasdaq"
    Then I should receive exchange details

  Scenario: List all exchanges
    When I request to list all exchanges
    Then I should receive a list of all exchanges

  Scenario: Get exchanges by currency
    When I search for exchanges by currency "USD"
    Then I should receive a list of exchanges using "USD"

  Scenario: List open exchanges
    When I request to list open exchanges
    Then I should receive a list of open exchanges

  Scenario: Get exchanges by polity
    When I search for exchanges by polity "EU"
    Then I should receive a list of exchanges in the "EU"
