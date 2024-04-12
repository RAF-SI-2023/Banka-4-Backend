package rs.edu.raf.service;

import rs.edu.raf.model.dto.CardNameDto;
import rs.edu.raf.model.dto.CardResponseDto;
import rs.edu.raf.model.dto.CreateCardDto;

import java.util.List;

/**
 * This interface defines methods for managing cards.
 */
public interface CardService {

    /**
     * Creates a new card.
     *
     * @param createCardDto The DTO containing information to create the card.
     * @return The card number of the created card.
     */
    public String createCard(CreateCardDto createCardDto);

    /**
     * Deletes a card by its card number.
     *
     * @param cardNumber The card number of the card to delete.
     */
    public void deleteCard(String cardNumber);

    /**
     * Blocks a card by its card number.
     *
     * @param cardNumber The card number of the card to block.
     */
    public void blockCard(String cardNumber);

    /**
     * Activates a card by its card number.
     *
     * @param cardNumber The card number of the card to activate.
     */
    public void activateCard(String cardNumber);

    /**
     * Deactivates a card by its card number.
     *
     * @param cardNumber The card number of the card to deactivate.
     */
    public void deactivateCard(String cardNumber);

    /**
     * Retrieves card names.
     *
     * @return The CardNameDto object containing card names.
     */
    public CardNameDto getCardNames();

    /**
     * Retrieves all cards.
     *
     * @return A list of CardResponseDto objects representing all cards.
     */
    public List<CardResponseDto> getAllCards();

    /**
     * Retrieves all cards for a specific user.
     *
     * @param userId The ID of the user whose cards are to be retrieved.
     * @return A list of CardResponseDto objects representing all cards for the specified user.
     */
    public List<CardResponseDto> getAllCardsForUser(Long userId);
}
