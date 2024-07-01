package rs.edu.raf.integration.account;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.model.dto.KorisnikDTO;
import rs.edu.raf.model.dto.RadnikDTO;
import rs.edu.raf.model.dto.racun.*;
import rs.edu.raf.model.entities.racun.DevizniRacun;
import rs.edu.raf.model.entities.racun.Firma;
import rs.edu.raf.model.entities.racun.PravniRacun;
import rs.edu.raf.model.entities.racun.TekuciRacun;
import rs.edu.raf.service.racun.RacunServis;

import java.math.BigDecimal;
import java.util.*;

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
    private DevizniRacun devizniRacun;
    private String userToken;
    private Firma firma;
    private PravniRacun pravniRacun;

    @Given("a worker with username {string} and password {string}")
    public void aWorkerWithUsernameAndPassword(String username, String password) {
        workerToken = login(username, password);
        worker = getWorkerByUsername(username);
    }

    @And("the account is available to the user")
    public void theAccountBecomesAvailableToTheUser() {
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

    @Given("a user with username {string} and password {string}")
    public void aUserWithUsernameAndPassword(String username, String password) {
        userToken = login(username, password);
        korisnik = getUserByUsername(username);
    }

    @And("the worker has created a new active account of type {string} for the user {string}")
    public void theWorkerHasCreatedAnAccountForTheUserWithBalance(String type, String email) {
        String getUrl = "http://localhost:8080/api/korisnik";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + workerToken);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(getUrl, HttpMethod.POST, request, String.class);

        String data = response.getBody();
        try {
            JSONArray JSonDtos = new JSONArray(data);
            for (int i = 0; i < JSonDtos.length(); i++){
                JSONObject o = JSonDtos.getJSONObject(i);
                if (email.equals(o.getString("email"))) {
                    korisnik = new KorisnikDTO();
                    korisnik.setId(Long.valueOf(o.getString("id")));
                    korisnik.setAdresa(o.getString("adresa"));
                    korisnik.setBrojTelefona(o.getString("brojTelefona"));
                    korisnik.setIme(o.getString("ime"));
                    korisnik.setPrezime(o.getString("prezime"));
                    korisnik.setEmail(email);
                    korisnik.setJmbg(Long.valueOf(o.getString("jmbg")));
                    korisnik.setDatumRodjenja(Long.valueOf(o.getString("datumRodjenja")));
                    korisnik.setPol(o.getString("pol"));
                    korisnik.setPovezaniRacuni(o.getString("povezaniRacuni"));
                    break;
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        NoviTekuciRacunDTO dto = new NoviTekuciRacunDTO();
        dto.setVlasnik(korisnik.getId());
        dto.setZaposleni(worker.getId());
        dto.setVrstaRacuna(type);
        tekuciRacun = racunServis.kreirajTekuciRacun(dto);
        assertNotNull(tekuciRacun);
    }

    @And("the user transfers {string} into account")
    public void theUserTransfersIntoAccount(String transfer) {
        racunServis.bankomat(tekuciRacun.getBrojRacuna(), BigDecimal.valueOf(Long.parseLong(transfer)));
        tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(tekuciRacun.getBrojRacuna());
        assertEquals(tekuciRacun.getStanje(),  BigDecimal.valueOf(Long.parseLong(transfer)));
    }

    @And("the user transfers {string} out of account")
    public void theUserTransfersOutOfAccount(String transfer) {
        BigDecimal oldStanje = tekuciRacun.getStanje();
        racunServis.bankomat(tekuciRacun.getBrojRacuna(), BigDecimal.valueOf(-Long.parseLong(transfer)));
        tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(tekuciRacun.getBrojRacuna());
        assertEquals(tekuciRacun.getStanje(), oldStanje.add(BigDecimal.valueOf(-Long.parseLong(transfer))));
    }

    @When("the worker deactivates the active account")
    public void theWorkerDeactivatesTheActiveAccount() {
        assertTrue(racunServis.deaktiviraj(tekuciRacun.getBrojRacuna()));
    }

    @Then("the account is marked as inactive in the user's accounts")
    public void theAccountIsMarkedAsInactiveInTheUsersAccounts() {
        List<RacunDTO> dtos = racunServis.izlistavanjeRacunaJednogKorisnika(korisnik.getId(), userToken);
        for (RacunDTO dto : dtos) {
            if (Long.parseLong(dto.getBrojRacuna()) == tekuciRacun.getBrojRacuna()) {
                assertFalse(dto.getAktivan());
            }
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

    @And("the worker has created an account for the firm")
    public void theWorkerCreatesAnAccountForTheFirm() {
        NoviPravniRacunDTO dto = new NoviPravniRacunDTO();
        dto.setZaposleni(worker.getId());
        dto.setFirma(firma.getId());
        pravniRacun = racunServis.kreirajPravniRacun(dto);
    }

    @And("the account is present in the firm's accounts")
    public void theAccountIsPresentInTheFirmsAccounts() {
        boolean f = false;
        List<RacunDTO> dtos = racunServis.izlistavanjeRacunaJednogKorisnika(firma.getId(), workerToken);
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
        List<RacunDTO> dtos = racunServis.izlistavanjeRacunaJednogKorisnika(firma.getId(), workerToken);
        for (RacunDTO dto : dtos) {
            if (Long.parseLong(dto.getBrojRacuna()) == pravniRacun.getBrojRacuna()) {
                assertFalse(dto.getAktivan());
            }
        }
    }

    @And("the worker has created a new foreign currency account for the user {string}")
    public void theWorkerHasCreatedANewForeignCurrencyAccountForTheUser(String email) {
        String getUrl = "http://localhost:8080/api/korisnik";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + workerToken);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(getUrl, HttpMethod.POST, request, String.class);

        String data = response.getBody();
        try {
            JSONArray JSonDtos = new JSONArray(data);
            for (int i = 0; i < JSonDtos.length(); i++){
                JSONObject o = JSonDtos.getJSONObject(i);
                if (email.equals(o.getString("email"))) {
                    korisnik = new KorisnikDTO();
                    korisnik.setId(Long.valueOf(o.getString("id")));
                    korisnik.setAdresa(o.getString("adresa"));
                    korisnik.setBrojTelefona(o.getString("brojTelefona"));
                    korisnik.setIme(o.getString("ime"));
                    korisnik.setPrezime(o.getString("prezime"));
                    korisnik.setEmail(email);
                    korisnik.setJmbg(Long.valueOf(o.getString("jmbg")));
                    korisnik.setDatumRodjenja(Long.valueOf(o.getString("datumRodjenja")));
                    korisnik.setPol(o.getString("pol"));
                    korisnik.setPovezaniRacuni(o.getString("povezaniRacuni"));
                    break;
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        NoviDevizniRacunDTO dto = new NoviDevizniRacunDTO();
        dto.setVlasnik(korisnik.getId());
        dto.setZaposleni(worker.getId());
        dto.setCurrency(Collections.singletonList("USD"));
        dto.setDefaultCurrency("RSD");
        dto.setBrojDozvoljenihValuta(2);
        devizniRacun = racunServis.kreirajDevizniRacun(dto);
        assertNotNull(devizniRacun);
    }

    @And("the foreign currency account is available to the user")
    public void theForeignCurrencyAccountIsAvailableToTheUser() {
        boolean f = false;
        List<RacunDTO> dtos = racunServis.izlistavanjeRacunaJednogKorisnika(korisnik.getId(), userToken);
        for (RacunDTO dto : dtos) {
            if (Long.parseLong(dto.getBrojRacuna()) == devizniRacun.getBrojRacuna()) {
                f = true;
            }
        }
        if (!f) {
            Assertions.fail("New foreign currency account not present in the user's account list");
        }
    }

    @And("the user transfers {string} into foreign currency account")
    public void theUserTransfersIntoForeignCurrencyAccount(String transfer) {
        racunServis.bankomat(devizniRacun.getBrojRacuna(), BigDecimal.valueOf(Long.parseLong(transfer)));
        devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(devizniRacun.getBrojRacuna());
        assertEquals(devizniRacun.getStanje(),  BigDecimal.valueOf(Long.parseLong(transfer)));
    }

    @And("the user transfers {string} out of foreign currency account")
    public void theUserTransfersOutOfForeignCurrencyAccount(String transfer) {
        BigDecimal oldStanje = devizniRacun.getStanje();
        assertFalse(racunServis.bankomat(devizniRacun.getBrojRacuna(), BigDecimal.valueOf(-Long.parseLong(transfer))));
        devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(devizniRacun.getBrojRacuna());
        assertEquals(devizniRacun.getStanje(), oldStanje);
    }

    @When("the worker deactivates the foreign currency account")
    public void theWorkerDeactivatesTheForeignCurrencyAccount() {
        assertTrue(racunServis.deaktiviraj(devizniRacun.getBrojRacuna()));
    }

    @Then("the foreign currency account is marked as inactive in the user's accounts")
    public void theForeignCurrencyAccountIsMarkedAsInactiveInTheUsersAccounts() {
        List<RacunDTO> dtos = racunServis.izlistavanjeRacunaJednogKorisnika(korisnik.getId(), userToken);
        for (RacunDTO dto : dtos) {
            if (Long.parseLong(dto.getBrojRacuna()) == devizniRacun.getBrojRacuna()) {
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

    private KorisnikDTO getUserByUsername(String username){
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

}
