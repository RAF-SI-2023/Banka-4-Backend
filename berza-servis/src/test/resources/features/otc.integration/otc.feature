Feature: OTC
  Preko ovih testova se testira end2end interakcija sa otc unutar banke

  Scenario: Prodavac postavlja deo svojih stockova na public za otc market, kupac pravi ponudu, potom banka i prodavac potvrdjuju
    Given prodavac ima stockove tickera "AAPL" i postavlja njih "50" na public
    When kupac postavi ponudu za "AAPL" stock kolicine "50" i banka i prodavac potvrde
    Then razmena se desi izmedju prodavca i kupca