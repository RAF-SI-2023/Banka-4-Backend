package rs.edu.raf.opcija.integration.opcijaservis;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.awaitility.Awaitility;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.opcija.dto.NovaOpcijaDto;
import rs.edu.raf.opcija.dto.OpcijaDto;
import rs.edu.raf.opcija.model.Korisnik;
import rs.edu.raf.opcija.model.KorisnikoveKupljeneOpcije;
import rs.edu.raf.opcija.model.OpcijaStanje;
import rs.edu.raf.opcija.model.OpcijaTip;
import rs.edu.raf.opcija.repository.KorisnikRepository;
import rs.edu.raf.opcija.repository.KorisnikoveKupljeneOpcijeRepository;
import rs.edu.raf.opcija.repository.OpcijaRepository;
import rs.edu.raf.opcija.servis.OpcijaServis;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.fail;

public class OpcijaServiceTestsSteps extends OpcijaServiceTestsConfig{


    @Autowired
    OpcijaServis opcijaServis;

    List<OpcijaDto> sveOpcijeList;
    List<OpcijaDto> opcijePoTicker;
    List<OpcijaDto> opcijePoDatumIsteka;
    List<OpcijaDto> opcijePoStrikePrice;
    OpcijaStanje opcijaStanje;
    KorisnikoveKupljeneOpcije korisnikoveKupljeneOpcije;
    OpcijaDto sacuvanaOpcija;

    @Autowired
    KorisnikRepository korisnikRepository;

    @Autowired
    KorisnikoveKupljeneOpcijeRepository korisnikoveKupljeneOpcijeRepository;

    KorisnikoveKupljeneOpcije korisnikoveKupljeneOpcijeState;
    Korisnik korisnikState;

    @Autowired
    OpcijaRepository opcijaRepository;

    @Given("api poziv za opcije je prethodno izvrsenn")
    public void apiPozivZaOpcijeJePrethodnoIzvrsenn() {

        await().atMost(20, SECONDS)
                .untilAsserted(() -> {
                    if (!opcijaRepository.findAll().iterator().hasNext()) {
                        fail("Nije pronađen nijedan rezultat u opcijaRepository.findAll() metodi");
                    }
                });
        //await().atMost(10, SECONDS).until(() -> opcijaRepository.findAll().iterator().hasNext());
        //ceka najvise zadatih sekudni dok se ne pojave podaci u opcijaRepository inace failuje
        //Awaitility.await().atMost(10, SECONDS).until(this::checkIfOpcijeAreLoaded);
    }
    /*private boolean checkIfOpcijeAreLoaded() {
        return !opcijaRepository.findAll().isEmpty();
    }*/


    @When("zahtevamo sve opcije")
    public void zahtevamoSveOpcije() {
        try {
            sveOpcijeList = opcijaServis.findAll();
            if(sveOpcijeList.size() == 0)
                fail("Ne postoje opcije");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @When("zahtevamo opciju sa tickerom {string}")
    public void zahtevamoOpcijuSaTickerom(String arg0) {
        opcijePoTicker = opcijaServis.findByStockAndDateAndStrike(arg0,null,null);
    }

    @Then("vraca se opcija sa tickerom {string}")
    public void vracaSeOpcijaSaTickerom(String arg0) {

        if(opcijePoTicker.size() == 0)
            fail("Ne postoje opcije po tickeru");

        for(OpcijaDto o:opcijePoTicker)
            if(!o.getTicker().equals(arg0))
                fail("Pogresan ticker");
    }

    @When("zahtevamo opciju sa datumom isteka {string}")
    public void zahtevamoOpcijuSaDatumomIsteka(String arg0) {
        opcijePoDatumIsteka = opcijaServis.findByStockAndDateAndStrike(null, LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.valueOf(arg0)), ZoneOffset.systemDefault()),null);
    }

    @Then("vraca se opcija sa datumom isteka {string}")
    public void vracaSeOpcijaSaDatumomIsteka(String arg0) {
        if(opcijePoDatumIsteka.size() == 0)
            fail("Ne postoje opcije po datumu isteka");

        for(OpcijaDto o:opcijePoDatumIsteka)
            if(o.getExpiration()!=Long.valueOf(arg0))
                fail("Pogresan datum isteka");
    }


    @When("zahtevamo opciju sa strike price {string}")
    public void zahtevamoOpcijuSaStrikePrice(String arg0) {
        opcijePoStrikePrice = opcijaServis.findByStockAndDateAndStrike(null, null,Double.valueOf(arg0));
    }

    @Then("vraca se opcija sa strike price {string}")
    public void vracaSeOpcijaSaStrikePrice(String arg0) {
        if(opcijePoStrikePrice.size() == 0)
            fail("Ne postoje opcije po strike price");


        for(OpcijaDto o:opcijePoStrikePrice)
            if(o.getStrikePrice()!=Double.valueOf(arg0))
                fail("Pogresan strike price");
    }

    @When("proveravamo stanje opcije sa id {string}")
    public void proveravamoStanjeOpcijeSaId(String arg0) {
        opcijaStanje = opcijaServis.proveriStanjeOpcije(Long.valueOf(arg0));
    }

    @Then("dobijamo stanje opcije")
    public void dobijamoStanjeOpcije() {
        if(opcijaStanje == null)
            fail("Ne postoji stanje opcije");
    }

    @When("izvrsavamo opciju sa id {string} i userId {string}")
    public void izvrsavamoOpcijuSaIdIUserId(String arg0, String arg1) {
        korisnikoveKupljeneOpcije = opcijaServis.izvrsiOpciju(Long.valueOf(arg0),Long.valueOf(arg1));
    }

    @And("dodamo opciju sa contractSymbol {string}")
    public void dodamoOpcijuSaContractSymbol(String arg0) {
        NovaOpcijaDto novaOpcijaDto = new NovaOpcijaDto();
        novaOpcijaDto.setContractSymbol("ABC");
        novaOpcijaDto.setTicker("AAPL");
        novaOpcijaDto.setOptionType(OpcijaTip.PUT);
        novaOpcijaDto.setOpenInterest(1000);
        novaOpcijaDto.setImpliedVolatility(1000);
        novaOpcijaDto.setBrojUgovora(1000);
        novaOpcijaDto.setSettlementDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(10000), ZoneOffset.systemDefault()));

        sacuvanaOpcija = opcijaServis.save(novaOpcijaDto);

    }

    @Then("vraca se opcija koja ima contractSymbol {string}")
    public void vracaSeOpcijaKojaImaContractSymbol(String arg0) {
        if(sacuvanaOpcija == null)
            fail("Ne postoji opcija sa tim contract symbol");
    }

    @Then("opcija je izvrsenaa")
    public void opcijaJeIzvrsenaa() {
        if(korisnikoveKupljeneOpcije == null)
            fail("Opcija nije izvrsena");
    }

    @And("nadji usera sa id {string} ili kreiraj usera")
    public void nadjiUseraSaIdIliKreirajUsera(String arg0) {
        korisnikState = korisnikRepository.findById(Long.valueOf(arg0)).orElse(null);
        if(korisnikState == null){
            korisnikState = new Korisnik();
            korisnikState = korisnikRepository.save(korisnikState);
        }
    }
    //opcijaid
    //korisnikid
    //iskoriscenafalse
    @And("nadji kupljenu upciju za usera sa id {string} ili kreiraj kupljenu upciju za usera sa id {string} i opciju sa id {string}")
    public void nadjiKupljenuUpcijuZaUseraSaIdIliKreirajKupljenuUpcijuZaUseraSaIdIOpcijuSaId(String arg0, String arg1, String arg2) {
        korisnikoveKupljeneOpcijeState = korisnikoveKupljeneOpcijeRepository.findFirstByKorisnikId(Long.valueOf(arg0)).orElse(null);
        if(korisnikoveKupljeneOpcijeState == null){
            korisnikoveKupljeneOpcijeState = new KorisnikoveKupljeneOpcije();
            korisnikoveKupljeneOpcijeState.setKorisnikId(Long.valueOf(arg0));
            korisnikoveKupljeneOpcijeState.setOpcijaId(Long.valueOf(arg2));
            korisnikoveKupljeneOpcijeState.setIskoriscena(false);
            korisnikoveKupljeneOpcijeState = korisnikoveKupljeneOpcijeRepository.save(korisnikoveKupljeneOpcijeState);
        }

    }
}
