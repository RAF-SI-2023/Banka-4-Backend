package rs.edu.raf.order.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.order.dto.OTCOfferDTO;
import rs.edu.raf.order.dto.OTCPlaceDTO;
import rs.edu.raf.order.dto.OTCResolveDTO;
import rs.edu.raf.order.exceptions.*;
import rs.edu.raf.order.service.OTCService;
import rs.edu.raf.order.service.UserStockService;
import rs.edu.raf.stocks.exceptions.ExceptionResponse;

import java.util.List;

@RestController
@RequestMapping("/otc")
@Tag(name = "OTC", description = "Over the counter exchange.")
@AllArgsConstructor
@SecurityRequirement(name = "jwt")
@CrossOrigin(origins = "*")
public class OTCController {

    private OTCService otcService;
    private UserStockService userStockService;

    @ApiOperation(value = "Returns all public available otc's.")
    @GetMapping("/all-public-otc/{id}")
    public ResponseEntity<List<?>> getAllPublicOTC(@PathVariable("id") Long id) {
        return new ResponseEntity<>(otcService.getAllPublicOTC(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Return user pending otc offers waiting for confirmation.")
    @GetMapping("/pending-otc-offers/{userId}")
    public ResponseEntity<List<?>> getAllPendingOTC(@PathVariable Long userId) {
        return new ResponseEntity<>(otcService.getAllPendingOTC(userId), HttpStatus.OK);
    }

    @ApiOperation(value = "Return user completed otc offers.")
    @GetMapping("/completed-otc-offers/{userId}")
    public ResponseEntity<List<?>> getAllCompletedOTC(@PathVariable Long userId) {
        return new ResponseEntity<>(otcService.getAllCompletedOTC(userId), HttpStatus.OK);
    }



    @ApiOperation(value = "Return all pending otc offers waiting for confirmation from bank.")
    @GetMapping("/pending-otc-offers/bank")
    public ResponseEntity<List<?>> getAllPendingOTCForBank() {
        return new ResponseEntity<>(otcService.getAllPendingOTCForBank(), HttpStatus.OK);
    }

    @ApiOperation(value = "Accept or decline pending otc offer.")
    @PostMapping("/resolve-otc")
    public ResponseEntity<?> resolveOTC(@RequestBody OTCResolveDTO otcResolveDTO) {
        try {
            otcService.resolveOTC(otcResolveDTO);
        } catch (OTCOfferDoesntExist | YouDontOwnThisOTC e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
        }


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Make a offer for a public otc.")
    @PutMapping("/make-offer-for-otc")
    public ResponseEntity<?> makeOfferForOTC(@RequestBody @Validated OTCOfferDTO otcOfferDTO) {
        try {
            otcService.makeOfferForOTC(otcOfferDTO);
        } catch (OTCOfferDoesntExist | QuantityOfOTCDoesntExist | PriceMustBePositive | OTCOfferCurrentlyPlaced e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Places provided amount of stock visible to public otc.")
    @PostMapping("/place-otc-public")
    public ResponseEntity<?> placeOTCPublic(@RequestBody @Validated OTCPlaceDTO otcPlaceDTO) {
        try {
//            otcService.placeOTCPublic(otcPlaceDTO, userId);
            userStockService.setPublicQuantity(otcPlaceDTO.getStockId(),otcPlaceDTO.getQuantity(),otcPlaceDTO.getUserId());
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
