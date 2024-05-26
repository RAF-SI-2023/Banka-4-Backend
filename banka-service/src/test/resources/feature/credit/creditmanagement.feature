Feature: Credit Management

  Scenario: User applies for a credit
    Given for credit a user with username "pstamenic7721rn@raf.rs" and password "123"
    When the user applies for a credit with the following details:
      | type    | amount | salary | currentEmploymentPeriod | loanTerm | branchOffice | bankAccountNumber | loanPurpose | permanentEmployee |
      | housing | 50000  | 3000   | 24                      | 120      | Main Branch  | 444000000900000033        | house       | true              |
    Then the user should see a message "Uspesno kreiran zahtev za kredit"

  Scenario: Worker approves the credit request
    Given for a credit a worker with username "pera@gmail.rs" and password "123"
    And the user with username "pstamenic7721rn@raf.rs" has created credit request
    When the worker approves the credit request for user
    Then the user should see a his credit request is approved
