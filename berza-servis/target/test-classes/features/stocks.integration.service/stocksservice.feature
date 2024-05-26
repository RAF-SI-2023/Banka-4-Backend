Feature: Service za stocks servis
  Izvrsava operacije nad stocks i dohvata ih sa apija

  Scenario: Dohvatanje svih stocks
    Given da su dodati stocks "IBM", "AAPL"
    When zatrazim sve stocks
    Then dobijem listu sa stocks "IBM", "AAPL"

  Scenario: Dohvatanje jednog stocka preko tickera
    Given postoji "IBM" stock u bazi
    When zatrazim stock sa tickerom "IBM"
    Then dobijem stock sa tickerom "IBM"

  Scenario: Dohvatanje stocka po last refreshu
    Given postoji stock sa nekim last refreshom u bazi
    When zatrazim stock sa tim last refreshom
    Then dobijem stockove sa tim last refreshom

  Scenario: Dohvatanje stocka po exchangeu
    Given postoji stock sa exchangeom "NYSE" u bazi
    When zatrazim stock sa exchangeom "NYSE"
    Then dobijem stockove sa exchangeom "NYSE"

  Scenario: Dodavanje stocka sa apija
    Given da postoji stock "AL" na apiju
    When dodam stock "AL" sa apija
    Then stock "AL" je dodat u bazu

  Scenario: Brisem stock iz baze
    Given da postoji stock "AL" u bazi
    When obrisem stock "AL" iz baze
    Then stock "AL" je obrisan iz baze

  Scenario: Trazim dailyStockHistory za stock
    When zatrazim dailyStockHistory za stock "NFLX"
    Then dobijem dailyStockHistory za stock "NFLX"

  Scenario: Trazim weeklyStockHistory za stock
    When zatrazim weeklyStockHistory za stock "NFLX"
    Then dobijem weeklyStockHistory za stock "NFLX"

  Scenario: Trazim monthlyStockHistory za stock
    When zatrazim monthlyStockHistory za stock "NFLX"
    Then dobijem monthlyStockHistory za stock "NFLX"