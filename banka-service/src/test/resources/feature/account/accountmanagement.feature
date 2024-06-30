Feature: Account Management

  Scenario: Worker logs in and creates active account
    Given a worker with username "pera@gmail.rs" and password "123"
    And a user with username "pstamenic7721rn@raf.rs"
    When the worker creates an active account of type "Poslovni" for the user
    Then the account becomes available to the user with username "pstamenic7721rn@raf.rs" and password "123"

  Scenario: User transfers money into account
    Given a user with username "pstamenic7721rn@raf.rs" and password "123"
    And the user has an active account with balance of "0"
    When the user transfers "5000" into account
    Then the account holds "5000" in balance

  Scenario: User money out of account
    Given a user with username "pstamenic7721rn@raf.rs" and password "123"
    And the user has an active account with balance of "5000"
    When the user transfers "2000" out of account
    Then the account holds "3000" in balance

  Scenario: Worker logs in and deactivates active account
    Given a worker with username "pera@gmail.rs" and password "123"
    And a user with username "pstamenic7721rn@raf.rs" and password "123"
    And the user has an active account
    When the worker deactivates the active account
    Then the account is marked as inactive in the user's accounts

  Scenario: Worker logs in and creates legal account
    Given a worker with username "pera@gmail.rs" and password "123"
    And a firm with the following details:
      | nazivPreduzeca | brojTelefona | brojFaksa | PIB | maticniBroj | sifraDelatnosti | registarskiBroj
      | Belit d.o.o. Beograd | 0112030403 | 0112030404 | 101017533 | 17328905 | 6102    | 130501701 |
    When the worker creates an account for the firm
    Then the account is present in the firm's accounts

  Scenario: Worker deactivates legal account
    Given a worker with username "pera@gmail.rs" and password "123"
    And the worker created an account for firm "Belit d.o.o. Beograd"
    When the worker deactivates the account
    Then the account is marked as inactive