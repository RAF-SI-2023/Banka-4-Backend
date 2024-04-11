Feature: Opcija servis

  Scenario: Dobijanje novododate opcije
    Given api poziv za opcije je prethodno izvrsenn
    And dodamo opciju sa contractSymbol "123"
    When zahtevamo sve opcije
    Then vraca se opcija koja ima contractSymbol "123"

  Scenario: Pretraga odredjene opcije po tickeru
    Given api poziv za opcije je prethodno izvrsenn
    When zahtevamo opciju sa tickerom "AA"
    Then vraca se opcija sa tickerom "AA"

  Scenario: Pretraga odredjene opcije po datumu isteka
    Given api poziv za opcije je prethodno izvrsenn
    When zahtevamo opciju sa datumom isteka "1713484800"
    Then vraca se opcija sa datumom isteka "1713484800"

  Scenario: Pretraga odredjene opcije po strike price
    Given api poziv za opcije je prethodno izvrsenn
    When zahtevamo opciju sa strike price "115.0"
    Then vraca se opcija sa strike price "115.0"


  Scenario: Proveravamo stanje opcije sa id "1"
    Given api poziv za opcije je prethodno izvrsenn
    When proveravamo stanje opcije sa id "1"
    Then dobijamo stanje opcije

  Scenario: Izvrsavamo opciju sa id "1" za usera sa id "1"
    Given api poziv za opcije je prethodno izvrsenn
    And nadji usera sa id "1" ili kreiraj usera
    And nadji kupljenu upciju za usera sa id "1" ili kreiraj kupljenu upciju za usera sa id "1" i opciju sa id "1"
    When izvrsavamo opciju sa id "1" i userId "1"
    Then opcija je izvrsenaa