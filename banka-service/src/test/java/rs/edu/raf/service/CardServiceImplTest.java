package rs.edu.raf.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.model.dto.CardNameDto;
import rs.edu.raf.model.dto.CardResponseDto;
import rs.edu.raf.model.dto.CreateCardDto;
import rs.edu.raf.model.entities.Card;
import rs.edu.raf.model.mapper.CardMapper;
import rs.edu.raf.repository.CardRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {


    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardServiceImpl cardService;


    @Test
    void createCard() {
        CreateCardDto createCardDto = new CreateCardDto();
        createCardDto.setName("visa");
        createCardDto.setLimit(BigDecimal.valueOf(5000));
        createCardDto.setType("visa");
        createCardDto.setBankAccountNumber("123456789");

        Card card = new Card();
        card.setName("TestName");
        card.setCardLimit(BigDecimal.valueOf(5000));
        card.setType("visa");
        card.setCreationDate(System.currentTimeMillis());
        card.setExpirationDate(System.currentTimeMillis() + 157680000000L);
        card.setStatus("active");
        card.setBankAccountNumber(4123451234567890L);
        card.setBlocked(false);
        card.setCvv("123");

        given(cardRepository.save(any(Card.class))).willReturn(card);

        String result = cardService.createCard(createCardDto);
        String expected = "Uspesno kreirana kartica";

        assertEquals(expected, result);
        if(!expected.equals(result)){
            fail("Expected " + expected);
        }

        verify(cardRepository, times(1)).save(any(Card.class));

    }

    @Test
    void deleteCard() {

    }

    @Test
    void blockCard() {
        Card card = new Card();
        card.setNumber("1234567890123456");
        card.setBlocked(false);

        given(cardRepository.findCardByNumber("1234567890123456")).willReturn(Optional.of(card));
        given(cardRepository.save(card)).willReturn(card);

        cardService.blockCard("1234567890123456");

        assertTrue(card.getBlocked());
        if(!card.getBlocked()){
            fail("Expected card to be blocked but it was not");
        }

        verify(cardRepository, times(1)).findCardByNumber("1234567890123456");
        verify(cardRepository, times(1)).save(card);

    }

    @Test
    void activateCard() {
        Card card = new Card();
        card.setNumber("1234567890123456");
        card.setBlocked(true);
        card.setStatus("inactive");

        given(cardRepository.findCardByNumber("1234567890123456")).willReturn(Optional.of(card));
        given(cardRepository.save(card)).willReturn(card);

        cardService.activateCard("1234567890123456");

        assertFalse(card.getBlocked());
        assertEquals("active", card.getStatus());
        if(card.getBlocked() || !"active".equals(card.getStatus())){
            fail("Expected card to be unblocked and status 'aktivna' but got blocked=" + card.getBlocked());
        }

        verify(cardRepository, times(1)).findCardByNumber("1234567890123456");
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void deactivateCard() {
        Card card = new Card();
        card.setNumber("1234567890123456");
        card.setStatus("active");

        given(cardRepository.findCardByNumber("1234567890123456")).willReturn(Optional.of(card));
        given(cardRepository.save(card)).willReturn(card);

        cardService.deactivateCard("1234567890123456");

        String expected = "inactive";
        assertEquals(expected, card.getStatus());

        if(!expected.equals(card.getStatus())){
            fail("Expected card status to be 'inactive', but got: " + card.getStatus());
        }

        verify(cardRepository, times(1)).findCardByNumber("1234567890123456");
        verify(cardRepository, times(1)).save(card);

    }

    @Test
    void getCardNames() {
        CardNameDto cardNameDto = cardService.getCardNames();

        assertNotNull(cardNameDto);
        assertEquals("mastercard,american_express,visa", cardNameDto.getName());
        if (cardNameDto == null || !"mastercard,american_express,visa".equals(cardNameDto.getName())) {
            fail("Expected card names to be 'mastercard,american_express,visa' but got: " + cardNameDto.getName());
        }
    }

    @Test
    void getAllCards() {
        Card card1 = new Card();
        card1.setNumber("1234567890123456");

        Card card2 = new Card();
        card2.setNumber("6543210987654321");

        List<Card> cards = List.of(card1, card2);
        List<CardResponseDto> cardResponseDtos = List.of(new CardResponseDto(), new CardResponseDto());

        given(cardRepository.findAll()).willReturn(cards);
        given(cardMapper.cardToCardResponseDto(card1)).willReturn(cardResponseDtos.get(0));
        given(cardMapper.cardToCardResponseDto(card2)).willReturn(cardResponseDtos.get(1));

        List<CardResponseDto> result = cardService.getAllCards();

        assertNotNull(result);
        assertEquals(2, result.size());

        if(result == null || result.size() != 2){
            fail("Expected list size to be 2 but got: " + (result != null ? result.size() : "null"));
        }

        verify(cardRepository, times(1)).findAll();
        verify(cardMapper, times(1)).cardToCardResponseDto(card1);
        verify(cardMapper, times(1)).cardToCardResponseDto(card2);

    }

    @Test
    void getAllCardsForUser() {
        Long userId = 1L;
        Card card1 = new Card();
        card1.setNumber("1234567890123456");

        Card card2 = new Card();
        card2.setNumber("6543210987654321");

        List<Card> cards = List.of(card1, card2);
        List<CardResponseDto> cardResponseDtos = List.of(new CardResponseDto(), new CardResponseDto());

        given(cardRepository.findAllCardsForUser(userId)).willReturn(cards);
        given(cardMapper.cardToCardResponseDto(card1)).willReturn(cardResponseDtos.get(0));
        given(cardMapper.cardToCardResponseDto(card2)).willReturn(cardResponseDtos.get(1));

        List<CardResponseDto> result = cardService.getAllCardsForUser(userId);

        assertNotNull(result);
        assertEquals(2, result.size());

        if(result == null || result.size() != 2){
            fail("Expected list size to be 2 but got: " + (result != null ? result.size() : "null"));
        }

        verify(cardRepository, times(1)).findAllCardsForUser(userId);
        verify(cardMapper, times(1)).cardToCardResponseDto(card1);
        verify(cardMapper, times(1)).cardToCardResponseDto(card2);
    }
}