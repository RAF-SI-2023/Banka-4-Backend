Feature: Stock controller
  Preko ovog kontrolera se dohvataju stock api podaci

  Scenario: Radnik dodaje novi stock u bazu ako ne postoji ili ga brise i dodaje iznova
    Given radnik se uspesno ulogovao, ima jwt token i dodaje stock sa tickerom "IBM" u bazu ako ne postoji vec
    When radnik doda novi stock sa tickerom "IBM"
    Then stock sa tickerom "IBM" se uspesno dodao u bazu

  Scenario: Radnik pokusa da doda stock sa pogresnim, nepostojecim tickerom
    Given radnik se uspesno ulogovao, ima jwt token
    When radnik pokusa da doda novi stock sa tickerom "HGJ"
    Then stock se nije dodao u bazu i radnik je obavesten o gresci

  Scenario: Radnik pokusa da doda stock koji je vec dodat u bazu
    Given radnik se uspesno ulogovao, ima jwt token i u bazi postoji stock sa tickeorm "IBM"
    When radnik pokusa da doda vec postojeci stock u bazu sa tickerom "IBM"
    Then radnik ce biti obavesten o gresci i stock nece biti dodat iznova

  Scenario: Radnik izlistava stock preko tickera, exchangea i lastRefresha, potom ga brise
    Given radnik se uspesno ulogovao, ima jwt token i dodao je u bazu stock sa tickerom "IBM"
    When radnik trazi stockove preko tickera, exchangea i lastRefresha tog stocka, a potom obrise taj stock
    Then bice izlistan taj stock svaki put za ta tri poziva
    And potom uspesno obrisan

  Scenario: Radnik dodaje stockove, potom ih izlistava i trazi dnevne, nedeljne i mesecne podatke za njih
    Given radnik se uspesno ulogovao, ima jwt token i stockovi sa sledecim tickerima "NFLX", "AAPL", "SONY" su u bazi, u suprotnom ih radnik dodaje
    When radnik izlista sve stockove i trazi dnevne, nedeljne i mesecne podatke za tickere "NFLX", "AAPL", "SONY"
    Then stockovi sa sledecim tickerima "NFLX", "AAPL", "SONY" su izlistani
    And dnevni, nedeljni i mesecni podaci su dohvaceni
