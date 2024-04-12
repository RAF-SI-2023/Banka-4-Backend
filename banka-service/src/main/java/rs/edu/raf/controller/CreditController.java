package rs.edu.raf.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.model.dto.CreditRequestDto;
import rs.edu.raf.model.dto.DetailedCreditDto;
import rs.edu.raf.service.CreditService;
import rs.edu.raf.service.CreditServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/credit")
@Tag(name = "Kartice", description = "Operacije nad kreditima")
@SecurityRequirement(name = "jwt")
@CrossOrigin(origins = "*")
public class CreditController {

    private final CreditService creditService;

    @Autowired
    public CreditController(CreditServiceImpl creditServiceImpl) {
        this.creditService = creditServiceImpl;
    }

    @ApiOperation(value = "Podnosenje zahteva za kredit uz prosledjivanje parametara " +
            "Svaki korisnik ima pravo da podnsese zahtev")
    @PostMapping("/apply")
    public ResponseEntity<String> applyForCredit(@RequestBody CreditRequestDto creditRequestDto){
        return new ResponseEntity(creditService.applyForCredit(creditRequestDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Odbijanje zahteva za kredit. Samo radnik ima pravo na ovu akciju.")
    @GetMapping("/deny/{id}")
    public ResponseEntity<String> denyCreditRequest(@PathVariable("id") Long id){
        return new ResponseEntity<>(creditService.dennyCreditRequest(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Odobravanje zahteva za kredit. Samo radnik ima pravo na ovu akciju.")
    @GetMapping("/approve/{id}")
    public ResponseEntity<String> approveCredit(@PathVariable("id") Long id){
        System.out.println("usao sam u kontroller ");
        return new ResponseEntity<>(creditService.approveCreditRequest(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Vraca kredite za sve korisnike. Korisnik vidi samo svoje kredite, Radnik moze da vidi sve kredite")
    @GetMapping("/all/{status}")
    public ResponseEntity<List<CreditRequestDto>> getAllCreditRequestsForUser(@RequestAttribute("userId") Long userId, @PathVariable("status") String status){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long permission = 0L;
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            List<? extends GrantedAuthority> authorityList = new ArrayList<>(authentication.getAuthorities());
            permission = Long.parseLong(authorityList.get(0).getAuthority());
            System.out.println("Permission iz CreditControllera" + permission);
        }

        if(permission == 0L)
            return new ResponseEntity<>(creditService.getAllCreditRequestForUser(userId, status), HttpStatus.OK);

        return new ResponseEntity<>(creditService.getAllCreditRequests(status), HttpStatus.OK);
    }

    @ApiOperation("Detaljan pregled kredita. Ova akcija je moguca samo ako je zahtev za taj kredit odobre. " +
            "Potrebno je proslediti id zahteva za kredit")
    @GetMapping("/detailed-credit/creditRequestId/{creditRequestId}")
    public ResponseEntity<DetailedCreditDto> getDetailedCredit(@PathVariable("creditRequestId") Long creditRequestId){
        return new ResponseEntity<>(creditService.getDetailedCredit(creditRequestId), HttpStatus.OK);
    }

}
