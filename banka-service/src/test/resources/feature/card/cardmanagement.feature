Feature: Card Management

  Scenario: User logs in and creates a card
    Given a user with username "pstamenic7721rn@raf.rs" and password "123"
    When the user creates a card with the following details:
      | type          | name       | bankAccountNumber   | limit    |
      | credit        | mastercard | 444000000900000033  | 5000.00  |
    Then the card should be created successfully

  Scenario: Worker logs in and blocks the card created by the user
    Given a worker with username "pera@gmail.rs" and password "123"
    And the user with username "pstamenic7721rn@raf.rs" has created a card
    When the worker blocks the card created by the user
    Then the user should see the card is blocked
