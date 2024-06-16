package rs.edu.raf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.edu.raf.model.entities.Card;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findCardByNumber(String number);
//    List<Card> findA

    @Override
    List<Card> findAll();

//    @Query("SELECT crd FROM Card crd WHERE crd.bankAccountNumber IN (SELECT tr.brojRacuna FROM TekuciRacun tr WHERE tr.vlasnik = :userId) " +
//            "OR crd.bankAccountNumber IN (SELECT dr.brojRacuna FROM DevizniRacun dr WHERE dr.vlasnik = :userId)")
    @Query("SELECT crd FROM Card crd JOIN Racun r ON crd.bankAccountNumber = r.brojRacuna WHERE r.vlasnik = :userId")
    public List<Card> findAllCardsForUser(@Param("userId") Long userId);

}
