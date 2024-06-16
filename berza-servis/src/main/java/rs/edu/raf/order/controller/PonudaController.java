package rs.edu.raf.order.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.order.dto.Banka3StockDTO;
import rs.edu.raf.order.dto.DodajPonuduDto;
import rs.edu.raf.order.dto.PonudaBanci3Dto;
import rs.edu.raf.order.dto.PonudaDTO;
import rs.edu.raf.order.model.StranaPonudaDTO;
import rs.edu.raf.order.service.PonudaService;
import java.util.List;

@RestController
@RequestMapping("/offer")
@Tag(name = "Offers", description = "Operations related to offers.")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PonudaController {

    private PonudaService ponudaService;

    @ApiOperation(value = "Get all available stocks from Banka 3.")
    @GetMapping("/see-stocks-from-banka3")
    @SecurityRequirement(name = "jwt")
    public ResponseEntity<?> getStocksFromBanka3() {
        List<Banka3StockDTO> res = ponudaService.dohvatiStokoveBanke3();
        if (res == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all available stocks from Banka 3.")
    @GetMapping("/banka3-offers")
    @SecurityRequirement(name = "jwt")
    public ResponseEntity<?> getAllOfers() {
        return new ResponseEntity<>(ponudaService.svePonude(), HttpStatus.OK);
    }

    @ApiOperation(value = "Place your offer here.")
    @PostMapping("/place-offer")
    public ResponseEntity<?> placeOffer(@RequestBody DodajPonuduDto dodajPonuduDto) {
        ponudaService.dodajPonudu(dodajPonuduDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Place your offer here.")
    @PostMapping("/place-to-banka3-offer")
    public ResponseEntity<?> placeToBanka3Offer(@RequestBody PonudaBanci3Dto dodajPonuduDto) {
        ponudaService.dodajPonuduBanci3(dodajPonuduDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Accept our offer.")
    @PostMapping("/accept-our-offer/{id}")
    public ResponseEntity<?> receiveOffer(@PathVariable("id") Long id) {
        ponudaService.potvrdiNasuPonudu(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Decline our offer.")
    @PostMapping("/decline-our-offer/{id}")
    public ResponseEntity<?> declineOurOffer(@PathVariable("id") Long id) {
        ponudaService.odbijNasuPonudu(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Accept banka's 3 offer.")
    @PostMapping("/accept-foreign-offer/{id}")
    @SecurityRequirement(name = "jwt")
    public ResponseEntity<?> acceptOffer(@PathVariable Long id) {
        ponudaService.prihvati(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/our-offers")
    public ResponseEntity<?> ourOffers() {
        return new ResponseEntity<>(ponudaService.svePonudeKaBanci3(),HttpStatus.OK);
    }

    @ApiOperation(value = "Decline banka's 3 offer.")
    @PostMapping("/decline-foreign-offer/{id}")
    @SecurityRequirement(name = "jwt")
    public ResponseEntity<?> declineOffer(@PathVariable Long id) {
        ponudaService.odbij(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
