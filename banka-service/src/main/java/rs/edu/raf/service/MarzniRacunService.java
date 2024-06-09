package rs.edu.raf.service;

import org.springframework.http.ResponseEntity;
import rs.edu.raf.model.dto.PairDTO;
import rs.edu.raf.model.dto.racun.MarzniRacunCreateDTO;
import rs.edu.raf.model.dto.racun.MarzniRacunUpdateDTO;
import rs.edu.raf.model.entities.racun.MarzniRacun;

import java.math.BigDecimal;

public interface MarzniRacunService {

    ResponseEntity<?> findAll();

    ResponseEntity<?> findALlByUserId(Long userId);

    ResponseEntity<?> bankProfit();

    ResponseEntity<?> createMarzniRacun(MarzniRacunCreateDTO marzniRacunCreateDTO);

    ResponseEntity<?> changeBalance(MarzniRacunUpdateDTO marzniRacunUpdateDTO);

    ResponseEntity<?> changeMaintenanceMargin(MarzniRacunUpdateDTO marzniRacunUpdateDTO);

    ResponseEntity<?> changeFundsFromOrder(PairDTO pairDTO);

    ResponseEntity<?> addFundsToMarzniRacun(MarzniRacunUpdateDTO marzniRacunUpdateDTO);

}
