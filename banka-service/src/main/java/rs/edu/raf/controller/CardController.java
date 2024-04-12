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
import rs.edu.raf.model.dto.CardNameDto;
import rs.edu.raf.model.dto.CardResponseDto;
import rs.edu.raf.model.dto.CreateCardDto;
import rs.edu.raf.service.CardService;
import rs.edu.raf.service.CardServiceImpl;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/cards")
@Tag(name = "Kartice", description = "Operacije nad karticama")
@SecurityRequirement(name = "jwt")
@CrossOrigin(origins = "*")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardServiceImpl cardServiceImpl) {
        this.cardService = cardServiceImpl;
    }

    @ApiOperation(value = "blokiranje kartice po broju kartice")
    @GetMapping("/{status}/{cardNumber}")
    public ResponseEntity<Void> blockCard(@PathVariable("status") String status,@PathVariable("cardNumber") String cardNumber){
        switch (status){
            case "aktivna" -> cardService.activateCard(cardNumber);
            case "deaktivirana" -> cardService.deactivateCard(cardNumber);
            case "blokirana" -> cardService.blockCard(cardNumber);
            default -> ResponseEntity.notFound().build();
        }
        cardService.blockCard(cardNumber);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Kreiranje kartice uz prosledjivanje parametara kroz request body")
    @PostMapping("/create")
    public ResponseEntity<String> createCard(@RequestBody CreateCardDto createCardDto){
        return new ResponseEntity<>(cardService.createCard(createCardDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Vraca kartice za korisnika. Admin ima mogucnost pregleda svih kartica za sve korisnike")
    @GetMapping
    public ResponseEntity<List<CardResponseDto>> getAllCards(@RequestAttribute("userId") Long userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long permission = 0L;
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            List<? extends GrantedAuthority> authorityList = new ArrayList<>(authentication.getAuthorities());
            permission = Long.parseLong(authorityList.get(0).getAuthority());
        }
        if(permission == 0L)
            return new ResponseEntity<>(cardService.getAllCardsForUser(userId), HttpStatus.OK);

        return new ResponseEntity<>(cardService.getAllCards(), HttpStatus.OK);
    }

    @ApiOperation(value = "vraca imena kartica (mastercard, visa, american express)")
    @GetMapping("/names")
    public ResponseEntity<CardNameDto> getCardNames(){
        return new ResponseEntity<>(cardService.getCardNames(), HttpStatus.OK);
    }

}
