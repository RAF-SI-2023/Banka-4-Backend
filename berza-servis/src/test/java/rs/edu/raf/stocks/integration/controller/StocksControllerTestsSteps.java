package rs.edu.raf.stocks.integration.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.stocks.dto.StockDTO;
import rs.edu.raf.stocks.exceptions.ExceptionResponse;
import rs.edu.raf.stocks.exceptions.StockAlreadyExistsException;
import rs.edu.raf.stocks.exceptions.TickerDoesntExist;
import rs.edu.raf.stocks.response.StockHistoryInfo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.fail;

public class StocksControllerTestsSteps extends StocksControllerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MvcResult mvcResult;

    private MvcResult lastRefreshMvcResult;

    private MvcResult exchangeMvcResult;

    private MvcResult dailyHistoryMvcResult;

    private MvcResult weeklyHistoryMvcResult;

    private MvcResult monthlyHistoryMvcResult;

    private String lastRefresh;

    private String exchange;

    private String ticker;

    private String jwtToken = "";

    private final String korisnikLoginEndpoint = "http://localhost:8080/api/korisnik/login";

    private final String username = "pera@gmail.rs";

    private final String password = "123";

    private final String loginRequestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

    @Given("radnik se uspesno ulogovao, ima jwt token i dodaje stock sa tickerom {string} u bazu ako ne postoji vec")
    public void radnikSeUspesnoUlogovaoIImaJwtToken(String ticker) {

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

        try {
            ResultActions resultActions = mockMvc.perform(get("/stock/" + ticker)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            mvcResult = resultActions.andReturn();

            String json = mvcResult.getResponse().getContentAsString();

            if (!json.isEmpty()){
                // Stock postoji u bazi vec pa ga brisemo
                ResultActions deleteAction = mockMvc.perform(delete("/stock/" + ticker)
                                .header("Authorization", "Bearer " + jwtToken))
                        .andExpect(status().isOk());

            }

        }
        catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @When("radnik doda novi stock sa tickerom {string}")
    public void radnikDodaNoviStockSaTickerom(String ticker) {
        try {
            ResultActions resultActions = mockMvc.perform(post("/stock")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content("{\"ticker\":\"" + ticker + "\"}")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Then("stock sa tickerom {string} se uspesno dodao u bazu")
    public void stockSeUspesnoDodaoUBazu(String ticker) {
        try {
            String json = mvcResult.getResponse().getContentAsString();

            StockDTO stock = objectMapper.readValue(json,  StockDTO.class);

            if (stock == null || !stock.getTicker().equals(ticker)) {
                fail("Stock with ticker " + ticker + "hasn't been added to the database!");
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Given("radnik se uspesno ulogovao, ima jwt token")
    public void radnikSeUspesnoUlogovaoImaJwtToken() {
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
    }

    @When("radnik pokusa da doda novi stock sa tickerom {string}")
    public void radnikPokusaDaDodaNoviStockSaTickerom(String ticker) {
        try {
            ResultActions resultActions = mockMvc.perform(post("/stock")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content("{\"ticker\":\"" + ticker + "\"}")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().is4xxClientError());

            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Then("stock se nije dodao u bazu i radnik je obavesten o gresci")
    public void stockSeNijeDodaoUBazuIRadnikJeObavestenOGresci() {
        try {
            String json = mvcResult.getResponse().getContentAsString();

            ExceptionResponse exceptionResponse = objectMapper.readValue(json,  ExceptionResponse.class);

            if (exceptionResponse.getMessage() == null || exceptionResponse.getMessage().isEmpty()) {
                fail("User didn't receive the warn message about the bad provided ticker.");
            }
        }
        catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Given("radnik se uspesno ulogovao, ima jwt token i u bazi postoji stock sa tickeorm {string}")
    public void radnikSeUspesnoUlogovaoImaJwtTokenIUBaziPostojiStockSaTickeorm(String ticker) {
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

        try {
            ResultActions resultActions = mockMvc.perform(get("/stock/" + ticker)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            mvcResult = resultActions.andReturn();
            String json = mvcResult.getResponse().getContentAsString();

            if (json.isEmpty()){
                // Dodaj stock ako ga vec nema u bazi
                mockMvc.perform(post("/stock")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"ticker\":\"" + ticker + "\"}")
                                .header("Authorization", "Bearer " + jwtToken))
                        .andExpect(status().isOk());
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

    }

    @When("radnik pokusa da doda vec postojeci stock u bazu sa tickerom {string}")
    public void radnikPokusaDaDodaVecPostojeciStockUBazu(String ticker) {
        try {
            ResultActions resultActions = mockMvc.perform(post("/stock")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content("{\"ticker\":\"" + ticker + "\"}")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().is4xxClientError());

            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Then("radnik ce biti obavesten o gresci i stock nece biti dodat iznova")
    public void radnikCeBitiObavestenOGresciIStockNeceBitiDodatIznova() {
        try {
            String json = mvcResult.getResponse().getContentAsString();

            ExceptionResponse exceptionResponse = objectMapper.readValue(json,  ExceptionResponse.class);

            if (exceptionResponse.getMessage() == null || exceptionResponse.getMessage().isEmpty()) {
                fail("User didn't receive the warn message about the duplicate stock adding.");
            }
        }
        catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Given("radnik se uspesno ulogovao, ima jwt token i dodao je u bazu stock sa tickerom {string}")
    public void radnikSeUspesnoUlogovaoImaJwtTokenIDodaoJeUBazuStockSaTickerom(String ticker) {
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

        try {
            ResultActions resultActions = mockMvc.perform(get("/stock/" + ticker)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            mvcResult = resultActions.andReturn();
            String json = mvcResult.getResponse().getContentAsString();

            if (json.isEmpty()){
                // Dodaj stock ako ga vec nema u bazi
                mockMvc.perform(post("/stock")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"ticker\":\"" + ticker + "\"}")
                                .header("Authorization", "Bearer " + jwtToken))
                        .andExpect(status().isOk());

                mvcResult = resultActions.andReturn();
                json = mvcResult.getResponse().getContentAsString();
            }

            StockDTO stock = objectMapper.readValue(json, StockDTO.class);

            this.lastRefresh = stock.getLastRefresh();
            this.exchange = stock.getExchange();
            this.ticker = ticker;

        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @When("radnik trazi stockove preko tickera, exchangea i lastRefresha tog stocka, a potom obrise taj stock")
    public void radnikTraziStockovePrekoExchangeaILastRefreshaTogStockaAPotomObriseTajStock() {
        try {
            ResultActions resultActions = mockMvc.perform(get("/stock/" + ticker)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            ResultActions lastRefreshResultActions = mockMvc.perform(get("/stock/all/date/" + lastRefresh)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            ResultActions exchangeResultActions = mockMvc.perform(get("/stock/all/exchange/" + exchange)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            ResultActions deleteAction = mockMvc.perform(delete("/stock/" + ticker)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            mvcResult = resultActions.andReturn();
            lastRefreshMvcResult = lastRefreshResultActions.andReturn();
            exchangeMvcResult = exchangeResultActions.andReturn();
        }
        catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Then("bice izlistan taj stock svaki put za ta tri poziva")
    public void biceIzlistanTajStock() {
        try {
            String tickerJson = mvcResult.getResponse().getContentAsString();
            String lastRefreshJson = lastRefreshMvcResult.getResponse().getContentAsString();
            String exchangeJson = exchangeMvcResult.getResponse().getContentAsString();

            StockDTO tickerStockDTO = objectMapper.readValue(tickerJson, StockDTO.class);
            List<StockDTO> lastRefreshStockDTO = objectMapper.readValue(lastRefreshJson, new TypeReference<List<StockDTO>>() {});
            List<StockDTO> exchangeStockDTO = objectMapper.readValue(exchangeJson, new TypeReference<List<StockDTO>>() {});

            if (!tickerStockDTO.getTicker().equals(ticker) || !lastRefreshStockDTO.contains(tickerStockDTO) || !exchangeStockDTO.contains(tickerStockDTO)){
                fail("One of the list functions didn't list out the required stock!");
            }
        }
        catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @And("potom uspesno obrisan")
    public void uspesnoObrisanPotom() {
        try {
            ResultActions resultActions = mockMvc.perform(get("/stock/" + ticker)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            mvcResult = resultActions.andReturn();
            String json = mvcResult.getResponse().getContentAsString();

            if (!json.isEmpty()){
                fail("The stock wasn't deleted.");
            }

        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Given("radnik se uspesno ulogovao, ima jwt token i stockovi sa sledecim tickerima {string}, {string}, {string} su u bazi, u suprotnom ih radnik dodaje")
    public void radnikSeUspesnoUlogovaoImaJwtTokenIStockoviSaSledecimTickerimaSuUBaziUSuprotnomIhRadnikDodaje(String ticker1, String ticker2, String ticker3) {
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

        try {
            ResultActions resultActions = mockMvc.perform(get("/stock/" + ticker1)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            mvcResult = resultActions.andReturn();
            String json = mvcResult.getResponse().getContentAsString();

            if (json.isEmpty()){
                // Dodaj stock ako ga vec nema u bazi
                mockMvc.perform(post("/stock")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"ticker\":\"" + ticker1 + "\"}")
                                .header("Authorization", "Bearer " + jwtToken))
                        .andExpect(status().isOk());
            }

            resultActions = mockMvc.perform(get("/stock/" + ticker2)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            mvcResult = resultActions.andReturn();
            json = mvcResult.getResponse().getContentAsString();

            if (json.isEmpty()){
                // Dodaj stock ako ga vec nema u bazi
                mockMvc.perform(post("/stock")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"ticker\":\"" + ticker2 + "\"}")
                                .header("Authorization", "Bearer " + jwtToken))
                        .andExpect(status().isOk());
            }

            resultActions = mockMvc.perform(get("/stock/" + ticker3)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            mvcResult = resultActions.andReturn();
            json = mvcResult.getResponse().getContentAsString();

            if (json.isEmpty()){
                // Dodaj stock ako ga vec nema u bazi
                mockMvc.perform(post("/stock")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .content("{\"ticker\":\"" + ticker3 + "\"}")
                                .header("Authorization", "Bearer " + jwtToken))
                        .andExpect(status().isOk());
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

    }

    @When("radnik izlista sve stockove i trazi dnevne, nedeljne i mesecne podatke za tickere {string}, {string}, {string}")
    public void radnikIzlistaSveStockoveITraziDnevneNedeljneIMesecnePodatke(String ticker1, String ticker2, String ticker3) {
        try {
            ResultActions resultActions = mockMvc.perform(get("/stock/all")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            ResultActions dailyActions = mockMvc.perform(get("/stock/dailyHistory/" + ticker1)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            ResultActions weeklyActions = mockMvc.perform(get("/stock/weeklyHistory/" + ticker2)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            ResultActions monthlyActions = mockMvc.perform(get("/stock/monthlyHistory/" + ticker3)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());

            mvcResult = resultActions.andReturn();
            dailyHistoryMvcResult = dailyActions.andReturn();
            weeklyHistoryMvcResult = weeklyActions.andReturn();
            monthlyHistoryMvcResult = monthlyActions.andReturn();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Then("stockovi sa sledecim tickerima {string}, {string}, {string} su izlistani")
    public void stockoviSaSledecimTickerimaSuIzlistani(String ticker1, String ticker2, String ticker3) {
        try {
            String json = mvcResult.getResponse().getContentAsString();
            List<StockDTO> allStocks = objectMapper.readValue(json, new TypeReference<List<StockDTO>>(){});

            int stocksToHaveCounter = 0;
            for (StockDTO stock : allStocks) {
                if (stock.getTicker().equals(ticker1) || stock.getTicker().equals(ticker2) || stock.getTicker().equals(ticker3)) {
                    stocksToHaveCounter += 1;
                }
            }

            if (stocksToHaveCounter != 3) {
                fail("Stocks that we're added to the database we're not listed by allStocks endpoint!");
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @And("dnevni, nedeljni i mesecni podaci su dohvaceni")
    public void dnevniNedeljniIMesecniPodaciSuDohvaceni() {
        try{
            String json1 = dailyHistoryMvcResult.getResponse().getContentAsString();
            String json2 = weeklyHistoryMvcResult.getResponse().getContentAsString();
            String json3 = monthlyHistoryMvcResult.getResponse().getContentAsString();

            Map<String, StockHistoryInfo> dailyHistory = objectMapper.readValue(json1,  Map.class);
            if (dailyHistory.isEmpty()) {
                fail("Daily history is empty");
            }

            Map<String, StockHistoryInfo> weeklyHistory = objectMapper.readValue(json2,  Map.class);
            if (weeklyHistory.isEmpty()) {
                fail("Weekly history is empty");
            }

            Map<String, StockHistoryInfo> monthlyHistory = objectMapper.readValue(json3,  Map.class);
            if (monthlyHistory.isEmpty()) {
                fail("Monthly history is empty");
            }

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}


