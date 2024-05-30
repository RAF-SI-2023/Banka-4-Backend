Feature: Marzni racun controller
  Ovaj controller radi sa marznim racunom

  Scenario: Korisnik pravi novi marzni racun, kupuje stock i time prelazi margin limit, potom izmiruje margin call prodajom stocka
    Given korisnik se ulogovao, napravio marzni racun i kupio stock cijom cenom prevazilazi margin limit
    When korisnik proda stockove cijom cenom zadovoljava margin limit
    Then margin call se iskljucio za korisnika