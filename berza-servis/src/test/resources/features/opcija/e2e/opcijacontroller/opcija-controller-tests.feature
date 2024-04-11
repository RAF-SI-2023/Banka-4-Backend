Feature: Kontroler za opcija servis



  Scenario: Gadjamo endpoint za dohvatanje svih opcija
    Given api poziv za opcije je prethodno izvrsen
    When gadjamo endpoint za dohvatanje svih opcija
    Then dobijemo sve opcije


  Scenario: Gadjamo endpoint za kreiranje opcije
    When gadjamo endpoint za kreiranje opcije
    Then dobijemo kreiranu opciju


  Scenario: Gadjamo endpoint da filtriramo opcije sa tickerom "AA"
    Given api poziv za opcije je prethodno izvrsen
    When gadjamo endpoint za filtriranje opcija sa tickerom "AA"
    Then dobijemo opcije sa tickerom "AA"

  Scenario: Gadjamo endpoint da filtriramo opcije sa datumIsteka "1713484800"
    Given api poziv za opcije je prethodno izvrsen
    When gadjamo endpoint za filtriranje opcija sa datumIsteka "1713484800"
    Then dobijemo opcije sa datumIsteka "1713484800"

  Scenario: Gadjamo endpoint da filtriramo opcije sa strikePrice "115.0"
    Given api poziv za opcije je prethodno izvrsen
    When gadjamo endpoint za filtriranje opcija sa strikePrice "115.0"
    Then dobijemo opcije sa strikePrice "115.0"

  Scenario: Gadjamo endpoint da filtriramo opcije sa tickerom "A" i sa datumIsteka "1713484800" i sa strikePrice "90.0"
    Given api poziv za opcije je prethodno izvrsen
    When gadjamo endpoint za filtriranje opcija sa tickerom "A" i datumIsteka "1713484800" i strikePrice "90.0"
    Then dobijemo opcije sa tickerom "A" i datumIsteka "1713484800" i strikePrice "90.0"

  Scenario: Gadjamo endpoint da proverimo stanje opcije sa id "2"
    Given api poziv za opcije je prethodno izvrsen
    When gadjamo endpoint da proverimo stanje opcije sa id "2"
    Then dobijemo stanje opcije

  Scenario: Gadjamo endpoint da izvrsimo opciju sa id "3" i userId "1"
    Given api poziv za opcije je prethodno izvrsen
    And nadji useraa sa id "1" ili kreiraj usera
    And nadji kupljenuu upciju za usera sa id "1" ili kreiraj kupljenu upciju za usera sa id "1" i opciju sa id "3"
    When gadjamo endpoint da izvrsimo opciju sa id "3" i userId "1"
    Then opcija je izvrsena
