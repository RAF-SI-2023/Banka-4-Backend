Feature: Account Management

  Scenario: Worker logs in and creates legal account
    Given a worker with username "pera@gmail.rs" and password "123"
    And a firm with legal name "Belit d.o.o. Beograd"
    When the worker creates an account for the firm
    Then the account is present in the firm's accounts

  Scenario: Worker deactivates legal account
    Given a worker with username "pera@gmail.rs" and password "123"
    And the worker created an account for firm "Belit d.o.o. Beograd"
    When the worker deactivates the account
    Then the account is marked as inactive