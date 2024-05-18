Feature: Korisnicki servis

  Scenario: Dodavanje novog korisnika
    When dodam novog korisnika
    Then novi korisnik se doda

#  Scenario: Registrovanje novog korisnika
#    When registrujem novog korisnika
#    Then novi korisnik se registruje

  Scenario: Promena sifre korisniku
    When promeni sifru korisniku
    Then sifra korisnika se promeni

  Scenario: Kreiranje novog radnika
    When kreiram novog radnika
    Then novi radnika je kreiran

  Scenario: Izmena korisnika
    When izmenim korisnika
    Then korisnik je izmenjen

  Scenario: Izmena radnika
    When izmenim radnika
    Then radnik je izmenjen

  Scenario: Izlistavanje svih aktivnih radnika
    When izlistam sve aktivne radnike
    Then svi aktivni radnici su izlistani

  Scenario: Izlistavanje svih aktivnih korisnika
    When izlistam sve aktivne korisnike
    Then svi aktivni korisnici su izlistani

  Scenario: Pronalazenje aktivnog radnika po emailu
    When pronadjem aktivnog radnika po emailu
    Then aktivni radnik je pronadjen

  Scenario: Pronalazenje aktivnog korisnika po emailu
    When pronadjem aktivnog korisnika po emailu
    Then aktivni korisnik je pronadjen a

  Scenario: Pronalazenje aktivnog korisnnika po JMBG
    When pronadjem aktivnog korisnika po JMBG
    Then aktivni korisnik je pronadjen b

  Scenario: Pronalazenje aktivnog korisnika po broju telefona
    When pronadjem aktivnog korisnika po broju telefona
    Then aktivni korisnik je pronadjen c