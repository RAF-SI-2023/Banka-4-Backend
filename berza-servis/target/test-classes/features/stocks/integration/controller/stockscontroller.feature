Feature: Stock controller
  Preko ovog kontrolera se dohvataju stock api podaci

  Scenario: Pozivam daily stock history endpoint
    When pozovem daily history endpoint sa "IBM"
    Then dobijem daily stock history podatke

  Scenario: Pozivam weekly stock history endpoint
    When pozovem weekly history endpoint sa "IBM"
    Then dobijem weekly stock history podatke

  Scenario: Pozivam monthly stock history endpoint
    When pozovem monthly history endpoint sa "IBM"
    Then dobijem monthly stock history podatke

  Scenario: Pozivam endpoint da dodam stock sa tickerom "SONY"
    When pozovem endpoint za dodavanje stocka sa tickerom "SONY"
    Then stock "SONY" je dodat u bazu

  Scenario: Pozivam endpoint da izlistam sve stockove iz baze
    When pozovem endpoint za listanje svih stockova
    Then dobijem listu svih stockova iz baze sa "SONY" stockom

  Scenario: Pozivam endpoint da dohvatim stock sa tickerom "SONY"
    When pozovem endpoint za dohvat stocka sa tickerom "SONY"
    Then dobijem stock sa tickerom "SONY"

  Scenario: Pozivam endpoint da izlistam stockove sa last refreshom
    When pozovem endpoint za listanje stockova sa last refreshom
    Then dobijem listu stockova sa last refreshom

  Scenario: Pozivam endpoint da izlistam stockove sa exchangeom "NYSE"
    When pozovem endpoint za listanje stockova sa exchangeom "NYSE"
    Then dobijem listu stockova sa exchangeom "NYSE"

  Scenario: Pozivam endpoint da izbrisem stock sa tickerom "SONY"
    When pozovem endpoint za brisanje stocka sa tickerom "SONY"
    Then stock "SONY" je izbrisan iz baze