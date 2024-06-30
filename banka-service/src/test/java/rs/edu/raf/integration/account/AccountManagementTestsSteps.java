package rs.edu.raf.integration.account;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.dto.KorisnikDTO;
import rs.edu.raf.model.dto.RadnikDTO;
import rs.edu.raf.model.dto.racun.NovaFirmaDTO;
import rs.edu.raf.model.dto.racun.NoviPravniRacunDTO;
import rs.edu.raf.model.dto.racun.NoviTekuciRacunDTO;
import rs.edu.raf.model.dto.racun.RacunDTO;
import rs.edu.raf.model.entities.racun.Firma;
import rs.edu.raf.model.entities.racun.PravniRacun;
import rs.edu.raf.model.entities.racun.TekuciRacun;
import rs.edu.raf.service.racun.RacunServis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AccountManagementTestsSteps extends AccountManagementConfig {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RacunServis racunServis;

    private String workerToken;
    private RadnikDTO worker;
    private KorisnikDTO korisnik;
    private TekuciRacun tekuciRacun;
    private String userToken;
    private Firma firma;
    private PravniRacun pravniRacun;

    @Given("a worker with username {string} and password {string}")
    public void aWorkerWithUsernameAndPassword(String username, String password) {
        workerToken = login(username, password);
        worker = getWorkerByUsername(username);
    }

    @Given("a user with username {string}")
    public void aUserWithUsername(String username) {
        String getUrl = "http://localhost:8080/api/korisnik";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(getUrl, HttpMethod.POST, request, String.class);

        List<KorisnikDTO> dtos = new ArrayList<>();
        String data = response.getBody();
        //String[] lines = data.split(",");
        //for (String k : lines) if username = k.get() korisnik = dto;
    }

    @When("the worker creates an active account of type {string} for the user")
    public void theWorkerCreatesAnActiveAccountForTheUser(String type) {
        NoviTekuciRacunDTO dto = new NoviTekuciRacunDTO();
        dto.setVlasnik(korisnik.getId());
        dto.setZaposleni(worker.getId());
        dto.setVrstaRacuna(type);
        tekuciRacun = racunServis.kreirajTekuciRacun(dto);
        assertNotNull(tekuciRacun);
    }

    @Then("the account becomes available to the user with username {string} and password {string}")
    public void theAccountBecomesAvailableToTheUser(String username, String password) {
        userToken = login(username, password);
        boolean f = false;
        List<RacunDTO> dtos = racunServis.izlistavanjeRacunaJednogKorisnika(korisnik.getId(), userToken);
        for (RacunDTO dto : dtos) {
            if (Long.parseLong(dto.getBrojRacuna()) == tekuciRacun.getBrojRacuna()) {
               f = true;
            }
        }
        if (!f) {
            Assertions.fail("New active account not present in the user's account list");
        }
    }

    @And("a firm with the following details:")
    public void aFirmWithTheFollowingDetails(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);

        NovaFirmaDTO dto = new NovaFirmaDTO();
        dto.setNazivPreduzeca(data.get(0).get("nazivPreduzeca"));
        dto.setBrojTelefona(data.get(0).get("brojTelefona"));
        dto.setBrojFaksa(data.get(0).get("brojFaksa"));
        dto.setPIB(Integer.valueOf(data.get(0).get("PIB")));
        dto.setMaticniBroj(Integer.valueOf(data.get(0).get("maticniBroj")));
        dto.setSifraDelatnosti(Integer.valueOf(data.get(0).get("sifraDelatnosti")));
        dto.setRegistarskiBroj(Integer.valueOf(data.get(0).get("registarskiBroj")));

        firma = racunServis.kreirajFirmu(dto);
    }

    @When("the worker creates an account for the firm")
    public void theWorkerCreatesAnAccountForTheFirm() {
        NoviPravniRacunDTO dto = new NoviPravniRacunDTO();
        dto.setZaposleni(worker.getId());
        dto.setFirma(firma.getId());
        pravniRacun = racunServis.kreirajPravniRacun(dto);
    }

    @Then("the account is present in the firm's accounts")
    public void theAccountIsPresentInTheFirmsAccounts() {
        boolean f = false;
        List<RacunDTO> dtos = racunServis.izlistavanjeRacunaJedneFirme(firma.getId());
        for (RacunDTO dto : dtos) {
            if (Long.parseLong(dto.getBrojRacuna()) == pravniRacun.getBrojRacuna()) {
                f = true;
            }
        }
        if (!f) {
            Assertions.fail("New account not present in the firm's account list");
        }
    }

    @When("the worker deactivates the account")
    public void theWorkerDeactivatesTheAccount() {
        assertTrue(racunServis.deaktiviraj(pravniRacun.getBrojRacuna()));
    }

    @Then("the account is marked as inactive")
    public void theAccountIsMarkedAsInactive() {
        List<RacunDTO> dtos = racunServis.izlistavanjeRacunaJedneFirme(firma.getId());
        for (RacunDTO dto : dtos) {
            if (Long.parseLong(dto.getBrojRacuna()) == pravniRacun.getBrojRacuna()) {
                assertFalse(dto.getAktivan());
            }
        }
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

    private RadnikDTO getWorkerByUsername(String username){
        String url = "http://localhost:8080/api/radnik/email/" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + workerToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<RadnikDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, RadnikDTO.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }

    }

}
