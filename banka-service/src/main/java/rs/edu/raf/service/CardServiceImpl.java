package rs.edu.raf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.edu.raf.model.dto.CardNameDto;
import rs.edu.raf.model.dto.CardResponseDto;
import rs.edu.raf.model.dto.CreateCardDto;
import rs.edu.raf.model.entities.Card;
import rs.edu.raf.model.mapper.CardMapper;
import rs.edu.raf.repository.CardRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CardServiceImpl implements CardService{
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper){
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }


    public String createCard(CreateCardDto createCardDto){

        Card card = new Card();

        card.setName(createCardDto.getName());
        card.setCardLimit(createCardDto.getLimit());
        card.setType(createCardDto.getType());


        LocalDate creationDate = LocalDate.now();
        LocalDate expirationDate = creationDate.plusMonths(60).withDayOfMonth(1);

        long creationDateMillis = creationDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        long expirationDateMillis = expirationDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        card.setCreationDate(creationDateMillis);
        card.setExpirationDate(expirationDateMillis);

        card.setStatus("active");

        card.setBankAccountNumber(Long.valueOf(createCardDto.getBankAccountNumber()));
        card.setNumber(generateCardNumber(createCardDto.getName().toLowerCase()));

        card.setBlocked(false);

        int rand = new Random().nextInt(900);
        card.setCvv(String.valueOf(rand + 100));

        cardRepository.save(card);

        return "Uspesno kreirana kartica";
    }



    public void deleteCard(String cardNumber){

        Optional<Card> optional = cardRepository.findCardByNumber(cardNumber);

        if(!optional.isPresent()){
            return;
        }

        Card card = optional.get();

        cardRepository.delete(card);

    }

    public void blockCard(String cardNumber){
        Optional<Card> optionalCard = cardRepository.findCardByNumber(cardNumber);
        if(!optionalCard.isPresent()){
            return;
        }

        Card card = optionalCard.get();
        card.setBlocked(true);
        cardRepository.save(card);
    }

    public void activateCard(String cardNumber){
        Optional<Card> optionalCard = cardRepository.findCardByNumber(cardNumber);
        if(!optionalCard.isPresent()){
            return;
        }

        Card card = optionalCard.get();
        card.setBlocked(false);
        card.setStatus("active");
        cardRepository.save(card);
    }

    public void deactivateCard(String cardNumber){
        Optional<Card> optionalCard = cardRepository.findCardByNumber(cardNumber);
        if(!optionalCard.isPresent()){
            return;
        }

        Card card = optionalCard.get();
        card.setStatus("inactive");
        cardRepository.save(card);
    }
    private String generateCardNumber(String type){
        String prefix;
        switch(type){
            case "mastercard":
                prefix = "2";
                break;
            case "visa":
                prefix = "4";
                break;
            case "american_express":
                prefix = "3";
                break;
            default:
                throw new IllegalArgumentException("Unsupported card type");
        }

        String middleDigits = "12345";

        String partialNumber = prefix + middleDigits;
        String cardNumber = partialNumber + generateLuhnCheckDigit(partialNumber);
        return cardNumber;
    }

    private String generateLuhnCheckDigit(String partialNumber){
        int s1 = 0;
        boolean alternate = false;
        for(int i = partialNumber.length() - 1; i >= 0; i--){
            int digit = Character.digit(partialNumber.charAt(i), 10);
            if(alternate){
                digit *= 2;
                if(digit > 9){
                    digit -= 9;
                }
            }
            alternate = !alternate;
            s1 += digit;
        }

        int mod = s1 % 10;
        int checkDigit = (mod == 0) ? 0 : (10 - mod);

        // Generisanje kontrolne sume od 10 cifara
        StringBuilder luhnPart = new StringBuilder();
        luhnPart.append(checkDigit); // Dodajemo prvu cifru koja je već izračunata
        Random random = new Random();
        for (int i = 1; i < 10; i++) {
            luhnPart.append(random.nextInt(10)); // Generišemo slučajne cifre za preostalih 9 cifara
        }
        return luhnPart.toString();
    }


    public CardNameDto getCardNames(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mastercard");
        stringBuilder.append(",");
        stringBuilder.append("american_express");
        stringBuilder.append(",");
        stringBuilder.append("visa");

        CardNameDto cardNameDto = new CardNameDto(stringBuilder.toString());

        return cardNameDto;
    }

    public List<CardResponseDto> getAllCards(){
        List<Card> cards = cardRepository.findAll();
        return cards.stream().map(cardMapper::cardToCardResponseDto).toList();
    }

    public List<CardResponseDto> getAllCardsForUser(Long userId){
        List<Card> cards = cardRepository.findAllCardsForUser(userId);
        return cards.stream().map(cardMapper::cardToCardResponseDto).toList();
    }

}
