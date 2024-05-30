package rs.edu.raf.controller.racun;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.model.dto.PairDTO;
import rs.edu.raf.model.dto.racun.MarzniRacunCreateDTO;
import rs.edu.raf.model.dto.racun.MarzniRacunUpdateDTO;
import rs.edu.raf.service.MarzniRacunService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/marzniRacuni")
@Tag(name = "MarzniRacuni", description = "Operacije nad marznim racunima")
@AllArgsConstructor
@SecurityRequirement(name = "jwt")
@CrossOrigin(origins = "*")
public class MarzniRacunController {

    private final MarzniRacunService marzniRacunService;


    @GetMapping
    public ResponseEntity<?> findAll() {
        return marzniRacunService.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> findAllByUserId(@PathVariable Long userId) {
        return marzniRacunService.findALlByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<?> createMarzniRacun(@RequestBody MarzniRacunCreateDTO marzniRacunCreateDTO) {
        return marzniRacunService.createMarzniRacun(marzniRacunCreateDTO);
    }

    @PutMapping("/balance")
    public ResponseEntity<?> changeBalance(@RequestBody MarzniRacunUpdateDTO marzniRacunUpdateDTO) {
        return marzniRacunService.changeBalance(marzniRacunUpdateDTO);
    }

    @PutMapping("/maintenanceMargin")
    public ResponseEntity<?> changeMaintenanceMargin(@RequestBody MarzniRacunUpdateDTO marzniRacunUpdateDTO) {
        return marzniRacunService.changeMaintenanceMargin(marzniRacunUpdateDTO);
    }

    // Ovo treba zastiti tako da moze samo OrderServiceImpl da ga zove
    @PutMapping("/updateBalance")
    public ResponseEntity<?> updateBalanceFromSoldStocks(@RequestBody PairDTO pairDTO) {
        return marzniRacunService.changeFundsFromOrder(pairDTO);
    }

    @PutMapping("/addFunds")
    public ResponseEntity<?> addFundsToMarzniRacun(@RequestBody MarzniRacunUpdateDTO marzniRacunUpdateDTO) {
        return marzniRacunService.changeMaintenanceMargin(marzniRacunUpdateDTO);
    }
}