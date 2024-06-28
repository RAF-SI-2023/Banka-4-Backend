package rs.edu.raf.otc.integration;

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
import rs.edu.raf.order.dto.OTCDTO;
import rs.edu.raf.order.dto.OTCOfferDTO;
import rs.edu.raf.order.dto.OTCPlaceDTO;
import rs.edu.raf.order.dto.OTCResolveDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OTCControllerTestsSteps extends OTCControllerConfig{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MvcResult mvcResult;

    private String jwtTokenProdavca = "";

    private String jwtTokenKupca = "";

    private final String korisnikLoginEndpoint = "http://localhost:8080/api/korisnik/login";

    // PRODAVAC
    private final String username = "pera@gmail.rs";

    private final String password = "123";

    private final String loginRequestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

    // KUPAC
    private final String usernameKupca = "pstamenic7721rn@raf.rs";

    private final String passwordKupca = "123";

    private final String loginRequestBodyKupca = "{\"username\": \"" + usernameKupca + "\", \"password\": \"" + passwordKupca + "\"}";

    @Given("prodavac ima stockove tickera {string} i postavlja njih {string} na public")
    public void prodavacImaStockoveTickeraIPostavljaNjihNaPublic(String ticker, String quantity) {

        //LOGIN PRODAVCA

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
                jwtTokenProdavca = loginResponse.body();
            } else if (loginResponse.statusCode() == 401){
                fail("Bad login credentials!");
            }
        }
        catch (IOException | InterruptedException e){
            // Korisnicki servis nije podignut.
            System.out.println(e);
        }



        // POSTAVLJANJE STOCKA NA PUBLIC (PRAVLJENJE OTC)

        OTCPlaceDTO otcPlaceDTO = new OTCPlaceDTO();
        // radnik smo u banci i postavljamo njihove stockove na otc
        otcPlaceDTO.setUserId(-1L);
        otcPlaceDTO.setTicker(ticker);
        otcPlaceDTO.setQuantity(Integer.valueOf(quantity));

        try{
            ResultActions resultActions = mockMvc.perform(post("/otc/place-otc-public")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(gson.toJson(otcPlaceDTO))
                            .header("Authorization", "Bearer " + jwtTokenProdavca))
                    .andExpect(status().isOk());

            mvcResult = resultActions.andReturn();

            String json = mvcResult.getResponse().getContentAsString();

            if (!json.isEmpty()){
                fail("We received a error: " + json);
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @When("kupac postavi ponudu za {string} stock kolicine {string} i banka i prodavac potvrde")
    public void kupacPostaviPonuduZaTeStockoveIBankaIProdavacPotvrde(String ticker, String quantity) {

        //LOGIN KUPCA

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create(korisnikLoginEndpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(loginRequestBodyKupca))
                .build();
        try {
            HttpResponse<String> loginResponse = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());

            if (loginResponse.statusCode() == 200){
                jwtTokenKupca = loginResponse.body();
            } else if (loginResponse.statusCode() == 401){
                fail("Bad login credentials!");
            }
        }
        catch (IOException | InterruptedException e){
            // Korisnicki servis nije podignut.
            System.out.println(e);
        }


        // IZLISTAVANJE DA POTVRDIMO DA SU STOCKOVI JAVNO DOSTUPNI

        Long otcIdToWhichWeWantToMakeTheOffer = 1L;

        try {
            ResultActions resultActions = mockMvc.perform(get("/otc/all-public-otc")
                            .header("Authorization", "Bearer " + jwtTokenKupca))
                    .andExpect(status().isOk());

            mvcResult = resultActions.andReturn();
            String json = mvcResult.getResponse().getContentAsString();

            List<OTCDTO> OTCDTOs = objectMapper.readValue(json, new TypeReference<List<OTCDTO>>() {});

            boolean containsStock = false;

            for (OTCDTO otc : OTCDTOs) {
                if (otc.getTicker().equals(ticker)){
                    otcIdToWhichWeWantToMakeTheOffer = otc.getOtcId();
                    containsStock = true;
                }
            }

            if (!containsStock){
                fail("OTC not visible publicly!");
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }


        // PRAVIMO PONUDU ZA OTC KOJI JE JAVNO DOSTUPAN

        Gson gson = new Gson();

        OTCOfferDTO otcOfferDTO = new OTCOfferDTO();
        // banka je prodavac
        otcOfferDTO.setOtcId(otcIdToWhichWeWantToMakeTheOffer);
        otcOfferDTO.setSellerId(-1L);
        otcOfferDTO.setBuyerId(1L);
        otcOfferDTO.setTicker(ticker);
        otcOfferDTO.setPriceOffered(new BigDecimal(1000));
        otcOfferDTO.setQuantity(Integer.valueOf(quantity));

        try{
            ResultActions resultActions = mockMvc.perform(put("/otc/make-offer-for-otc")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(gson.toJson(otcOfferDTO))
                            .header("Authorization", "Bearer " + jwtTokenKupca))
                    .andExpect(status().isOk());

            mvcResult = resultActions.andReturn();

            String json = mvcResult.getResponse().getContentAsString();

            if (!json.isEmpty()){
                fail("We received a error: " + json);
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }


        // PROVERA DA PRODAVAC (BANKA U OVOM SLUCAJU) IMA U LISTI PONUDU KOJU JE KUPAC NAPRAVIO

        try {
            ResultActions resultActions = mockMvc.perform(get("/otc/pending-otc-offers/-1")
                            .header("Authorization", "Bearer " + jwtTokenProdavca))
                    .andExpect(status().isOk());

            mvcResult = resultActions.andReturn();
            String json = mvcResult.getResponse().getContentAsString();

            List<OTCOfferDTO> OTCOfferDTOs = objectMapper.readValue(json, new TypeReference<List<OTCOfferDTO>>() {});

            boolean containsStock = false;

            for (OTCOfferDTO otc : OTCOfferDTOs) {
                if (otc.getTicker().equals(ticker)){
                    if (!Objects.equals(otc.getOtcId(), otcIdToWhichWeWantToMakeTheOffer)){
                        fail("Id of the otc offer is not the same?!");
                    }
                    containsStock = true;
                }
            }

            if (!containsStock){
                fail("OTC offer from buyer not visible to seller!");
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }


        // POTVRDJUJEMO PONUDU KAO PRODAVAC

        OTCResolveDTO otcResolveDTO = new OTCResolveDTO();
        otcResolveDTO.setUserId(-1L);
        otcResolveDTO.setOtcId(otcIdToWhichWeWantToMakeTheOffer);
        otcResolveDTO.setAccept(true);

        try{
            ResultActions resultActions = mockMvc.perform(post("/otc/resolve-otc")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content(gson.toJson(otcResolveDTO))
                            .header("Authorization", "Bearer " + jwtTokenProdavca))
                    .andExpect(status().isOk());

            mvcResult = resultActions.andReturn();

            String json = mvcResult.getResponse().getContentAsString();

            if (!json.isEmpty()){
                fail("We received a error: " + json);
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Then("razmena se desi izmedju prodavca i kupca")
    public void razmenaSeDesiIzmedjuProdavcaIKupca() {
    }

}
