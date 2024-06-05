package rs.edu.raf.racun.marzni.integration.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.model.dto.racun.MarzniRacunCreateDTO;
import rs.edu.raf.model.dto.racun.MarzniRacunUpdateDTO;
import rs.edu.raf.model.dto.racun.RacunDTO;
import rs.edu.raf.request.OrderRequest;
import rs.edu.raf.response.OrderResponse;
import rs.edu.raf.service.racun.RacunServis;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MarzniRacunControllerTestsSteps extends MarzniRacunControllerConfig{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RacunServis racunServis;

    private MvcResult mvcResult;

    private String jwtToken = "";

    private final String korisnikLoginEndpoint = "http://localhost:8080/api/korisnik/login";

    private final String username = "pstamenic7721rn@raf.rs";

    //private final String username = "pera@gmail.rs";

    private final String password = "123";

    private final String loginRequestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";


    private MarzniRacunCreateDTO marzniRacunToBeCreated;

    private MarzniRacunUpdateDTO marzniRacunUpdateDTO;

    private final Long idKorisnika = 1L;

    @Given("korisnik se ulogovao, napravio marzni racun i kupio stock cijom cenom prevazilazi margin limit")
    public void korisnikSeUlogovaoNapravioMarzniRacunIKupioStockCijomCenomPrevazilaziMarginLimit() {


        //LOGIN


        Gson gson = new Gson();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create(korisnikLoginEndpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(loginRequestBody))
                .build();
        try {
            HttpResponse<String> loginResponse = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());

            if (loginResponse.statusCode() == 200){
                jwtToken = loginResponse.body();
            } else if (loginResponse.statusCode() == 401){
                fail("Bad login credentials!");
            }
        }
        catch (IOException | InterruptedException e){
            // Korisnicki servis nije podignut.
            System.out.println(e);
        }


        //KREIRANJE RACUNA

        List<RacunDTO> racun = null;
        try {
            ResultActions resultActions = mockMvc.perform(get("/racuni/nadjiRacuneKorisnika/" + idKorisnika)
                            .header("Authorization", "Bearer " + jwtToken))
                      .andExpect(status().is2xxSuccessful());

            mvcResult = resultActions.andReturn();

            String json = mvcResult.getResponse().getContentAsString();

            racun = objectMapper.readValue(json,  new TypeReference<List<RacunDTO>>() {});
        } catch (Exception e) {
            fail("Failed to create MarzniRacun: " + e);
        }

        if (racun == null) {
            fail("Bill for user not found!");
        }

        marzniRacunToBeCreated = new MarzniRacunCreateDTO();
        // Nije sigurno da je 1
        marzniRacunToBeCreated.setVlasnik(idKorisnika);
        marzniRacunToBeCreated.setValuta("RSD");
        marzniRacunToBeCreated.setBrojRacuna(Long.valueOf(racun.get(0).getBrojRacuna()));
        marzniRacunToBeCreated.setGrupaHartija("STOCKS");
        // Promeniti po potrebi
        marzniRacunToBeCreated.setMaintenanceMargin(new BigDecimal(1000));

        try {
            ResultActions resultActions = mockMvc.perform(post("/marzniRacuni")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(gson.toJson(marzniRacunToBeCreated))
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().is2xxSuccessful());

            mvcResult = resultActions.andReturn();

            String json = mvcResult.getResponse().getContentAsString();

            System.out.println(json);
        } catch (Exception e) {
            fail("Failed to create MarzniRacun: " + e);
        }


        // KUPOVINA STOCKA

        String buyStockOrderControllerEndpoint = "http://localhost:8081/api/orders/place-order";

        OrderRequest request = new OrderRequest();
        request.setUserId(idKorisnika);
        request.setTicker("AAPL");
        request.setQuantity(1);
        request.setAction("BUY");
        //dodaj ako je potrebno

        HttpRequest sellStockRequest = HttpRequest.newBuilder()
                .uri(URI.create(buyStockOrderControllerEndpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                .build();

        try {
            HttpResponse<String> sellStockResponse = client.send(sellStockRequest, HttpResponse.BodyHandlers.ofString());

            OrderResponse response = gson.fromJson(sellStockResponse.body(), OrderResponse.class);
        } catch (Exception e) {
            fail("Error buying the stock");
        }
    }

    @When("korisnik proda stockove cijom cenom zadovoljava margin limit")
    public void korisnikProdaStockoveCijomCenomZadovoljavaMarginLimit() {

        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        marzniRacunUpdateDTO = new MarzniRacunUpdateDTO();
        // Ne mora biti 1
        marzniRacunUpdateDTO.setUserId(idKorisnika);
        marzniRacunUpdateDTO.setGrupaHartija("STOCKS");
        marzniRacunUpdateDTO.setAmount(new BigDecimal(999));

        // Provera margin call da li je postavljen
        try {
            ResultActions resultActions = mockMvc.perform(put("/marzniRacuni/balance")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content("{\n" +
                                    "  \"userId\": 1,\n" +
                                    "  \"grupaHartija\": \"STOCKS\",\n" +
                                    "  \"amount\": 999,\n" +
                                    "}")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().is4xxClientError());

            mvcResult = resultActions.andReturn();

            String json = mvcResult.getResponse().getContentAsString();

            System.out.println(json);
        } catch (Exception e){
            fail("Error trying to check the Margin call field!");
        }

        // Prodaja stocka da se iskljuci margin call
        String buyStockOrderControllerEndpoint = "http://localhost:8081/api/orders/place-order";

        OrderRequest request = new OrderRequest();
        request.setUserId(idKorisnika);
        request.setTicker("AAPL");
        request.setQuantity(1);
        request.setAction("SELL");
        //dodaj ako je potrebno

        HttpRequest sellStockRequest = HttpRequest.newBuilder()
                .uri(URI.create(buyStockOrderControllerEndpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                .build();

        try {
            HttpResponse<String> sellStockResponse = client.send(sellStockRequest, HttpResponse.BodyHandlers.ofString());

            OrderResponse response = gson.fromJson(sellStockResponse.body(), OrderResponse.class);

        } catch (Exception e) {
            fail("Error buying the stock");
        }
    }

    @Then("margin call se iskljucio za korisnika")
    public void marginCallSeIskljucioZaKorisnika() {
        // Provera margin call da li je postavljen
        try {
            ResultActions resultActions = mockMvc.perform(put("/marzniRacuni/balance")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content("{\n" +
                                    "  \"userId\": 1,\n" +
                                    "  \"grupaHartija\": \"STOCKS\",\n" +
                                    "  \"amount\": 1000\n" +
                                    "}")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().is2xxSuccessful());

            mvcResult = resultActions.andReturn();
        } catch (Exception e){
            fail("Error trying to check the Margin call field!");
        }
    }

}
