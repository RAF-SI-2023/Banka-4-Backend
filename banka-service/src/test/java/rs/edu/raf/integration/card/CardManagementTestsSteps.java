package rs.edu.raf.integration.card;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.model.dto.CardResponseDto;
import rs.edu.raf.model.dto.CreateCardDto;
import rs.edu.raf.model.dto.KorisnikDTO;
import rs.edu.raf.service.CardService;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardManagementTestsSteps extends CardManagementTestsConfig{

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CardService cardService;

    private String userToken;
    private String workerToken;
    private String cardNumber;
    private Long userId;
    private String bankAccountNumber;
    private String createCardMsg;
    private String blockCardMsg;

    private boolean isBlocked;

    @Given("a user with username {string} and password {string}")
    public void aUserWithUsernameAndPassword(String username, String password) {
        userToken = login(username, password);
    }

    @When("the user creates a card with the following details:")
    public void theUserCreatesACardWithTheFollowingDetails(io.cucumber.datatable.DataTable dataTable) {
//        List<List<String>> data = dataTable.asList((Type)String.class);
//        CreateCardDto createCardDto = new CreateCardDto(
//                data.get(1).get(1),
//                data.get(2).get(1),
//                data.get(3).get(1),
//                new BigDecimal(data.get(4).get(1))
//        );
//
//        bankAccountNumber = data.get(2).get(1);
//
//        createCardMsg = cardService.createCard(createCardDto);

        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);

        CreateCardDto createCardDto = new CreateCardDto(
                data.get(0).get("type"),
                data.get(0).get("name"),
                data.get(0).get("bankAccountNumber"),
                new BigDecimal(data.get(0).get("limit"))
        );

        bankAccountNumber = data.get(0).get("bankAccountNumber");

        createCardMsg = cardService.createCard(createCardDto);
    }

    @Then("the card should be created successfully")
    public void theCardShouldBeCreatedSuccessfully() {
        Assertions.assertEquals("Uspesno kreirana kartica", createCardMsg);
    }

    @Given("a worker with username {string} and password {string}")
    public void aWorkerWithUsernameAndPassword(String username, String password) {
        workerToken = login(username, password);
    }


    @When("the worker blocks the card created by the user")
    public void theWorkerBlocksTheCardCreatedByTheUser() {
        List<CardResponseDto> cardResponseDtos = getAllCardsByUserId(userId);
        if(cardResponseDtos == null){
            Assertions.fail("Card for specified user doesn't exists!");
        }

        if(cardResponseDtos.size() != 1){
            Assertions.fail("Something went wrong with cards for user");
        }


        CardResponseDto cardResponseDto = cardResponseDtos.get(0);
        cardNumber = cardResponseDto.getNumber();
        cardService.blockCard(cardResponseDto.getNumber());
    }

    @Then("the user should see the card is blocked")
    public void theUserShouldSeeTheCardIsBlocked() {
        List<CardResponseDto> cardResponseDtos = getAllCardsByUserId(userId);
        if(cardResponseDtos == null){
            Assertions.fail("Card for specified user doesn't exists!");
        }

        if(cardResponseDtos.size() != 1){
            Assertions.fail("Something went wrong with cards for user!");
        }

        CardResponseDto cardResponseDto = cardResponseDtos.get(0);
        Assertions.assertEquals(true, cardResponseDto.getBlocked(), "User see that his card is blocked by worker!");
    }


    private String login(String username, String password){
        String loginUrl = "http://localhost:8080/api/korisnik/login";

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", username);
        loginRequest.put("password", password);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(loginRequest, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(loginUrl, HttpMethod.POST, request, String.class);

        return response.getBody();
    }

    private KorisnikDTO getUserByUsername(String username){
//        String url = "http://localhost:8080/api/korisnik/email/" + username;
//
//        ResponseEntity<KorisnikDTO> response = restTemplate.getForEntity(url, KorisnikDTO.class);
//
//        if(response.getStatusCode() == HttpStatus.OK){
//            return response.getBody();
//        }else{
//            return null;
//        }
        String url = "http://localhost:8080/api/korisnik/email/" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + workerToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<KorisnikDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, KorisnikDTO.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }

    }

    private List<CardResponseDto> getAllCardsByUserId(Long id){
        return cardService.getAllCardsForUser(userId);
    }

    @And("the user with username {string} has created a card")
    public void theUserWithUsernameHasCreatedACard(String username) {
        KorisnikDTO korisnikDTO = getUserByUsername(username);
        userId = korisnikDTO.getId();
    }

    @After
    public void cleanUp(){
        if(cardNumber != null){
            cardService.deleteCard(cardNumber);
        }
    }
}
