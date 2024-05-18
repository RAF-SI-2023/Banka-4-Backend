package rs.edu.raf.korisnik.integration.service;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.dto.*;
import rs.edu.raf.exceptions.UserNotFoundException;
import rs.edu.raf.model.Korisnik;
import rs.edu.raf.servis.KorisnikServis;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;


@Disabled
public class UserServiceTestsSteps {
}
//
//public class UserServiceTestsSteps extends UserServiceConfig{
//
//    @Autowired
//    KorisnikServis korisnikServis;
//
//    private String emailKorisnika;
//
//    private String staraSifra;
//
//    private List<RadnikDTO> radniciDTO;
//
//    private List<KorisnikDTO> korisniciDTO;
//
//    private RadnikDTO pronadjenRadnikDTO;
//
//    private KorisnikDTO pronadjenKorisnikDTO;
//
//    private NoviKorisnikDTO createMockNoviKorisnikDTO() {
//        NoviKorisnikDTO noviKorisnikDTO = new NoviKorisnikDTO();
//        noviKorisnikDTO.setIme("Pera");
//        noviKorisnikDTO.setPrezime("Peric");
//
//        noviKorisnikDTO.setJmbg(String.valueOf(1805000793457L));
//
//        noviKorisnikDTO.setDatumRodjenja(958618800000L);
//        noviKorisnikDTO.setPol("M");
//        noviKorisnikDTO.setEmail("nekiemail@gmail.com");
//        noviKorisnikDTO.setBrojTelefona("0621234567");
//        noviKorisnikDTO.setAdresa("Bulevar Kralja Petra 1");
//        noviKorisnikDTO.setAktivan(true);
//
//        return noviKorisnikDTO;
//    }
//
//    private Korisnik createMockKorisnik(){
//        Korisnik korisnik = new Korisnik();
//        korisnik.setId(0L);
//        korisnik.setIme("Pera");
//        korisnik.setPrezime("Peric");
//
//        korisnik.setJmbg(String.valueOf(1805000793457L));
//
//        korisnik.setDatumRodjenja(958618800000L);
//        korisnik.setPol("M");
//        korisnik.setEmail("nekiemail@gmail.com");
//        korisnik.setBrojTelefona("0621234567");
//        korisnik.setAdresa("Bulevar Kralja Petra 1");
//        korisnik.setPassword("123");
//        korisnik.setPovezaniRacuni("pera1234");
//        korisnik.setAktivan(true);
//
//        return korisnik;
//    }
//
//    private NoviRadnikDTO createMockNoviRadnikDTO() {
//        NoviRadnikDTO noviRadnikDTO = new NoviRadnikDTO();
//        noviRadnikDTO.setIme("Pera");
//        noviRadnikDTO.setPrezime("Peric");
//
//        noviRadnikDTO.setJmbg(String.valueOf(1705000793457L));
//
//        noviRadnikDTO.setDatumRodjenja(958532400000L);
//        noviRadnikDTO.setPol("M");
//        noviRadnikDTO.setEmail("nekiemail@gmail.com");
//        noviRadnikDTO.setBrojTelefona("0621234567");
//        noviRadnikDTO.setAdresa("Bulevar Kralja Petra 1");
//        noviRadnikDTO.setUsername("pera");
//        noviRadnikDTO.setPassword("123");
//        noviRadnikDTO.setPozicija("Blagajnik");
//        noviRadnikDTO.setDepartman("Racunovodstvo");
//        noviRadnikDTO.setPermisije(1L);
//        noviRadnikDTO.setAktivan(true);
//
//        return noviRadnikDTO;
//    }
//
//    private IzmenaKorisnikaDTO createMockIzmenaKorisnikDTO() {
//        IzmenaKorisnikaDTO izmenaKorisnikaDTO = new IzmenaKorisnikaDTO();
//        izmenaKorisnikaDTO.setPrezime("Peric");
//        izmenaKorisnikaDTO.setPol("M");
//        izmenaKorisnikaDTO.setEmail("email@gmail.com");
//        izmenaKorisnikaDTO.setBrojTelefona("0611234567");
//        izmenaKorisnikaDTO.setAdresa("Bulevar Kralja Nikole 1");
//        izmenaKorisnikaDTO.setPassword("123");
//        izmenaKorisnikaDTO.setPovezaniRacuni("pera1234");
//        izmenaKorisnikaDTO.setAktivan(true);
//
//        return izmenaKorisnikaDTO;
//    }
//
//    private IzmenaRadnikaDTO createMockIzmenaRadnikDTO() {
//        IzmenaRadnikaDTO izmenaRadnikaDTO = new IzmenaRadnikaDTO();
//        izmenaRadnikaDTO.setPrezime("Peric");
//        izmenaRadnikaDTO.setPol("M");
//        izmenaRadnikaDTO.setBrojTelefona("0611234567");
//        izmenaRadnikaDTO.setAdresa("Bulevar Kralja Nikole 1");
//        izmenaRadnikaDTO.setPassword("123");
//        izmenaRadnikaDTO.setPozicija("Blagajnik");
//        izmenaRadnikaDTO.setDepartman("Racunovodstvo");
//        izmenaRadnikaDTO.setPermisije(1L);
//        izmenaRadnikaDTO.setAktivan(true);
//
//        return izmenaRadnikaDTO;
//    }
//
//    @When("dodam novog korisnika")
//    public void dodamNovogKorisnika() {
//
//        NoviKorisnikDTO noviKorisnikDTO = createMockNoviKorisnikDTO();
//
//        emailKorisnika = noviKorisnikDTO.getEmail();
//
//        korisnikServis.kreirajNovogKorisnika(noviKorisnikDTO);
//    }
//
//
//    @Then("novi korisnik se doda")
//    public void noviKorisnikSeDoda() {
//
//        KorisnikDTO korisnikDTO = korisnikServis.nadjiAktivnogKorisnikaPoEmail(emailKorisnika);
//
//        if(korisnikDTO == null){
//            fail("Korisnik nije dodat u bazu!");
//        }
//    }
//
////    @When("registrujem novog korisnika")
////    public void registrujemNovogKorisnika() {
////        RegistrujKorisnikDTO registrujKorisnikDTO = new RegistrujKorisnikDTO();
////
////        registrujKorisnikDTO.setEmail("nekiemail@gmail.com");
////        registrujKorisnikDTO.setBrojTelefona("064pera1234567");
////        registrujKorisnikDTO.setPassword("123");
////        registrujKorisnikDTO.setBrojRacuna("2312");
////        registrujKorisnikDTO.setCode("kod");
////
////        emailKorisnika = "nekiemail@gmail.com";
////
////        korisnikServis.registrujNovogKorisnika(registrujKorisnikDTO);
////    }
////
////    @Then("novi korisnik se registruje")
////    public void noviKorisnikSeRegistruje() {
////
////        KorisnikDTO korisnikDTO = korisnikServis.nadjiAktivnogKorisnikaPoEmail(emailKorisnika);
////
////        if(korisnikDTO == null){
////            fail("Korisnik nije registrovan u bazu!");
////        }
////    }
//
//
//    @When("promeni sifru korisniku")
//    public void promenikSifruKorisniku() {
//
//        staraSifra = "123";
//        String novaSifra = "pera123";
//
//        IzmenaSifreDto izmenaSifreDto = new IzmenaSifreDto();
//        izmenaSifreDto.setStaraSifra(staraSifra);
//        izmenaSifreDto.setNovaSifra(novaSifra);
//
//        korisnikServis.promeniSifruKorisnika("pstamenic7721rn@raf.rs", izmenaSifreDto);
//    }
//
//    @Then("sifra korisnika se promeni")
//    public void sifraKorisnikaSePromeni() {
//
//        staraSifra = "123";
//        String novaSifra = "pera123";
//
//        IzmenaSifreDto izmenaSifreDto = new IzmenaSifreDto();
//        izmenaSifreDto.setStaraSifra(staraSifra);
//        izmenaSifreDto.setNovaSifra(novaSifra);
//
//        try {
//            korisnikServis.promeniSifruKorisnika("pstamenic7721rn@raf.rs", izmenaSifreDto);
//        }
//        catch (UserNotFoundException e){
//            return;
//        }
//        fail("Sifra korisniku nije promenjena!");
//    }
//
//    @When("kreiram novog radnika")
//    public void kreiramNovogRadnika() {
//
//        NoviRadnikDTO noviRadnikDTO = createMockNoviRadnikDTO();
//
//        emailKorisnika = noviRadnikDTO.getEmail();
//
//        korisnikServis.kreirajNovogRadnika(noviRadnikDTO);
//    }
//
//    @Then("novi radnika je kreiran")
//    public void noviRadnikaJeKreiran() {
//
//        RadnikDTO radnikDTO = korisnikServis.nadjiAktivnogRadnikaPoEmail(emailKorisnika);
//
//        if(radnikDTO == null){
//            fail("Radnik nije kreiran u bazi!");
//        }
//    }
//
//
//    @When("izmenim korisnika")
//    public void izmenimKorisnika() {
//
//        IzmenaKorisnikaDTO izmenaKorisnikaDTO = createMockIzmenaKorisnikDTO();
//
//        emailKorisnika = izmenaKorisnikaDTO.getEmail();
//
//        izmenaKorisnikaDTO.setId(1L);
//
//        korisnikServis.izmeniKorisnika(izmenaKorisnikaDTO);
//    }
//
//    @Then("korisnik je izmenjen")
//    public void korisnikJeIzmenjen() {
//        KorisnikDTO korisnikDTO = korisnikServis.nadjiAktivnogKorisnikaPoEmail(emailKorisnika);
//
//        if(korisnikDTO == null){
//            fail("Korisnik nije kreiran u bazi!");
//        }
//
//        if (!korisnikDTO.getAdresa().equals("Bulevar Kralja Nikole 1")){
//            fail("Korisnik nije izmenjen u bazi!");
//        }
//    }
//
//
//    @When("izmenim radnika")
//    public void izmenimRadnika() {
//        IzmenaRadnikaDTO izmenaRadnikaDTO = createMockIzmenaRadnikDTO();
//
//        emailKorisnika = "nekiemail@gmail.com";
//
//        izmenaRadnikaDTO.setId(2L);
//
//        korisnikServis.izmeniRadnika(izmenaRadnikaDTO);
//    }
//
//    @Then("radnik je izmenjen")
//    public void radnikJeIzmenjen() {
//        RadnikDTO radnikDTO = korisnikServis.nadjiAktivnogRadnikaPoEmail(emailKorisnika);
//
//        if(radnikDTO == null){
//            fail("Radnik nije kreiran u bazi!");
//        }
//
//        if (!radnikDTO.getAdresa().equals("Bulevar Kralja Nikole 1")){
//            fail("Radnik nije izmenjen u bazi!");
//        }
//    }
//
//
//    @When("izlistam sve aktivne radnike")
//    public void izlistamSveAktivneRadnike() {
//        radniciDTO = korisnikServis.izlistajSveAktivneRadnike();
//    }
//
//    @Then("svi aktivni radnici su izlistani")
//    public void sviAktivniRadniciSuIzlistani() {
//        if (radniciDTO == null){
//            fail("Radnici nisu izlistani!");
//        }
//    }
//
//
//    @When("izlistam sve aktivne korisnike")
//    public void izlistamSveAktivneKorisnike() {
//        korisniciDTO = korisnikServis.izlistajSveAktivneKorisnike();
//    }
//
//    @Then("svi aktivni korisnici su izlistani")
//    public void sviAktivniKorisniciSuIzlistani() {
//        if (korisniciDTO == null){
//            fail("Korisnici nisu izlistani!");
//        }
//    }
//
//    @When("pronadjem aktivnog radnika po emailu")
//    public void pronadjemAktivnogRadnikaPoEmailu() {
//        pronadjenRadnikDTO = korisnikServis.nadjiAktivnogRadnikaPoEmail("nekiemail@gmail.com");
//    }
//
//    @Then("aktivni radnik je pronadjen")
//    public void aktivniRadnikJePronadjen() {
//        if (pronadjenRadnikDTO == null){
//            fail("Radnik nije pronadjen!");
//        }
//    }
//
//    @When("pronadjem aktivnog korisnika po emailu")
//    public void pronadjemAktivnogKorisnikaPoEmailu() {
//        pronadjenKorisnikDTO = korisnikServis.nadjiAktivnogKorisnikaPoEmail("nekiemail@gmail.com");
//    }
//
//    @Then("aktivni korisnik je pronadjen a")
//    public void aktivniKorisnikJePronadjena() {
//        if (pronadjenKorisnikDTO == null) {
//            fail("Korisnik nije pronadjen!");
//        }
//    }
//
//    @When("pronadjem aktivnog korisnika po JMBG")
//    public void pronadjemAktivnogKorisnikaPoJMBG() {
//        pronadjenKorisnikDTO = korisnikServis.nadjiAktivnogKorisnikaPoJMBG(String.valueOf(1705000793457L));
//    }
//
//    @Then("aktivni korisnik je pronadjen b")
//    public void aktivniKorisnikJePronadjenb() {
//        if (pronadjenKorisnikDTO == null) {
//            fail("Korisnik nije pronadjen!");
//        }
//    }
//
//    @When("pronadjem aktivnog korisnika po broju telefona")
//    public void pronadjemAktivnogKorisnikaPoBrojuTelefona() {
//        pronadjenKorisnikDTO = korisnikServis.nadjiAktivnogKorisnikaPoBrojuTelefona("+381600176998");
//    }
//
//    @Then("aktivni korisnik je pronadjen c")
//    public void aktivniKorisnikJePronadjenC() {
//        if (pronadjenKorisnikDTO == null) {
//            fail("Korisnik nije pronadjen!");
//        }
//    }
//}
