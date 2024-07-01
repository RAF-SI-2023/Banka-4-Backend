Feature: Account Management

  Scenario: Worker logs in and deactivates active account
    Given a user with username "pstamenic7721rn@raf.rs" and password "123"
    And a worker with username "pera@gmail.rs" and password "123"
    And the worker has created a new active account of type "Poslovni" for the user "pstamenic7721rn@raf.rs"
    And the account is available to the user
    And the user transfers "5000" into account
    And the user transfers "2000" out of account
    When the worker deactivates the active account
    Then the account is marked as inactive in the user's accounts

  Scenario: Worker logs in and creates legal account
    Given a worker with username "pera@gmail.rs" and password "123"
    And a firm with the following details:
      | nazivPreduzeca | brojTelefona | brojFaksa | PIB | maticniBroj | sifraDelatnosti | registarskiBroj
      | Belit d.o.o. Beograd | 0112030403 | 0112030404 | 101017533 | 17328905 | 6102    | 130501701 |
    And the worker has created an account for the firm
    And the account is present in the firm's accounts
    When the worker deactivates the account
    Then the account is marked as inactive

  Scenario: Worker logs in and deactivates foreign currency account
    Given a user with username "pstamenic7721rn@raf.rs" and password "123"
    And a worker with username "pera@gmail.rs" and password "123"
    And the worker has created a new foreign currency account for the user "pstamenic7721rn@raf.rs"
    And the foreign currency account is available to the user
    And the user transfers "2000" into foreign currency account
    And the user transfers "3000" out of foreign currency account
    When the worker deactivates the foreign currency account
    Then the foreign currency account is marked as inactive in the user's accounts