package rs.edu.raf.exchange.integration;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.exchange.controller.ExchangeController;
import rs.edu.raf.exchange.dto.ExchangeDTO;
import rs.edu.raf.exchange.service.ExchangeService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ExchangeControllerTestsSteps /*extends ExchangeControllerTestsConfig*/{

    /*@Autowired
    private TestRestTemplate restTemplate;*/
//
//    private ResponseEntity<ExchangeDTO> exchangeByNameResponse;
//    private ResponseEntity<List<ExchangeDTO>> allExchangesResponse;
//    private ResponseEntity<List<ExchangeDTO>> exchangesByCurrencyResponse;
//    private ResponseEntity<List<ExchangeDTO>> openExchangesResponse;
//    private ResponseEntity<List<ExchangeDTO>> exchangesByPolityResponse;
//
//    @Autowired
//    private ExchangeController exchangeController;

    /*@Autowired
    private ExchangeService exchangeService;*/

//    @Given("the exchange service is available")
//    public void theExchangeServiceIsAvailable() {
//    }
//
//    @When("I search for exchange by name {string}")
//    public void iSearchForExchangeByName(String exchangeName) {
//        exchangeByNameResponse = exchangeController.getExchangeByName(exchangeName);
//    }
//
//    @Then("I should receive exchange details")
//    public void iShouldReceiveExchangeDetails() {
//        assertEquals(200, exchangeByNameResponse.getStatusCodeValue());
//    }
//
//    @When("I request to list all exchanges")
//    public void iRequestToListAllExchanges() {
//        allExchangesResponse = exchangeController.listAllExchanges();
//    }
//
//    @Then("I should receive a list of all exchanges")
//    public void iShouldReceiveAListOfAllExchanges() {
//        assertEquals(200, allExchangesResponse.getStatusCodeValue());
//    }
//
//    @When("I search for exchanges by currency {string}")
//    public void iSearchForExchangesByCurrency(String currency) {
//        exchangesByCurrencyResponse = exchangeController.getExchangesByCurrency(currency);
//    }
//
//    @Then("I should receive a list of exchanges using {string}")
//    public void iShouldReceiveAListOfExchangesUsingCurrency(String currency) {
//        assertEquals(200, exchangesByCurrencyResponse.getStatusCodeValue());
//    }
//
//    @When("I request to list open exchanges")
//    public void iRequestToListOpenExchanges() {
//        openExchangesResponse = exchangeController.listOpenExchanges();
//    }
//
//    @Then("I should receive a list of open exchanges")
//    public void iShouldReceiveAListOfOpenExchanges() {
//        assertEquals(200, openExchangesResponse.getStatusCodeValue());
//    }
//
//    @When("I search for exchanges by polity {string}")
//    public void iSearchForExchangesByPolity(String polity) {
//        exchangesByPolityResponse = exchangeController.getExchangesByPolity(polity);
//    }
//
//    @Then("I should receive a list of exchanges in the {string}")
//    public void iShouldReceiveAListOfExchangesInPolity(String polity) {
//        assertEquals(200, exchangesByPolityResponse.getStatusCodeValue());
//    }


}
