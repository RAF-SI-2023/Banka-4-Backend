package rs.edu.raf.futures.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.futures.dto.FutureRequestDto;
import rs.edu.raf.futures.service.FuturesService;
import rs.edu.raf.futures.dto.FuturesContractDto;

import java.util.List;

@RestController
@RequestMapping("/futures")
@AllArgsConstructor
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "jwt")
public class FuturesController {
    private FuturesService futuresService;

    @GetMapping("/type/{type}")
    @Operation(description = "Get all future contracts by type")
    public ResponseEntity<List<FuturesContractDto>> allFuturesByType(@PathVariable("type") String type) {
        return new ResponseEntity<>(futuresService.findByType(type), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    @Operation(description = "Get all future contracts by name")
    public ResponseEntity<List<FuturesContractDto>> allFuturesByName(@PathVariable("name") String name) {
        return new ResponseEntity<>(futuresService.findByName(name),HttpStatus.OK);
    }

    @GetMapping("/kupac/{id}")
    @Operation(description = "Get all future contracts for user")
    public ResponseEntity<List<FuturesContractDto>> allFuturesForUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(futuresService.findByKupac(id), HttpStatus.OK);
    }

    @GetMapping("/request")
    @Operation(description = "Get all requests for buying future contracts")
    public ResponseEntity<List<FutureRequestDto>> allRequests(@RequestAttribute("userId") Long radnik_id) {
        return new ResponseEntity<>(futuresService.allRequests(radnik_id),HttpStatus.OK);
    }

    @PutMapping("/approve/{id}")
    @Operation(description = "Approve buying future contract")
    public void approve(@PathVariable("id") Long id, @RequestAttribute("userId") Long supervisor_id) {
        futuresService.approveRequest(id,supervisor_id);
    }

    @PutMapping("/deny/{id}")
    @Operation(description = "Deny request for buying future contracts")
    public void denyRequest(@PathVariable("id") Long id) {
        futuresService.denyRequest(id);
    }

    @PostMapping("/buy/{id}/{racun}")
    @Operation(description = "Buy future contract")
    public ResponseEntity<FuturesContractDto> buyFuture(@PathVariable("id") Long id, @PathVariable("racun") String racun, @RequestAttribute("userId") Long userId) {
        return new ResponseEntity<>(futuresService.buy(id,userId,racun),HttpStatus.OK);
    }
}
