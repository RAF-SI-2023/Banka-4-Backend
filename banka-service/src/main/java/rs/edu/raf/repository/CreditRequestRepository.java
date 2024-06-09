package rs.edu.raf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.edu.raf.model.entities.CreditRequest;

import java.util.List;

public interface CreditRequestRepository extends JpaRepository<CreditRequest, Long> {

    @Query("SELECT cr FROM CreditRequest cr WHERE cr.status = 'approved'")
    List<CreditRequest> findApprovedCreditRequests();

    @Query("SELECT cr FROM CreditRequest cr WHERE cr.status = 'denied'")
    List<CreditRequest> findDeniedCreditRequests();

    @Query("SELECT cr FROM CreditRequest cr WHERE cr.status = 'not_approved'")
    List<CreditRequest> findNotApprovedCreditRequests();

    List<CreditRequest> findAllByStatus(String status);

//    @Query("SELECT cr FROM CreditRequest cr WHERE (cr.bankAccountNumber IN (SELECT tr.brojRacuna FROM TekuciRacun tr WHERE tr.vlasnik = :userId) " +
//            "OR cr.bankAccountNumber IN (SELECT dr.brojRacuna FROM DevizniRacun dr WHERE dr.vlasnik = :userId)) AND cr.status = :status")
    @Query("SELECT cr FROM CreditRequest cr JOIN Racun r ON cr.bankAccountNumber = r.brojRacuna WHERE r.vlasnik = :userId AND cr.status = :status")
    List<CreditRequest> findAllCreditRequestsForUser(@Param("userId") Long userId, @Param("status") String status);

}
