package rs.edu.raf.model.mapper;


import org.springframework.stereotype.Component;
import rs.edu.raf.model.dto.CardResponseDto;
import rs.edu.raf.model.entities.Card;

import java.math.BigDecimal;

@Component
public class CardMapper {

    public CardResponseDto cardToCardResponseDto(Card card){

        CardResponseDto cardResponseDto = new CardResponseDto();

        cardResponseDto.setCardLimit(card.getCardLimit());
        cardResponseDto.setCvv(card.getCvv());
        cardResponseDto.setBlocked(card.getBlocked());
        cardResponseDto.setBankAccountNumber(card.getBankAccountNumber().toString());
        cardResponseDto.setNumber(card.getNumber());
        cardResponseDto.setCreationDate(card.getCreationDate());
        cardResponseDto.setExpirationDate(card.getExpirationDate());
        cardResponseDto.setType(card.getType());
        cardResponseDto.setStatus(card.getStatus());
        cardResponseDto.setBlocked(card.getBlocked());
        cardResponseDto.setName(card.getName());


        return cardResponseDto;

    }

}
