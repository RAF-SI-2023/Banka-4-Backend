package rs.edu.raf.korisnik;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import rs.edu.raf.dto.*;
import rs.edu.raf.exceptions.UserNotFoundException;
import rs.edu.raf.exceptions.WrongEmployeeException;
import rs.edu.raf.mapper.KorisnikMapper;
import rs.edu.raf.mapper.RadnikMapper;
import rs.edu.raf.model.Korisnik;
import rs.edu.raf.model.Radnik;
import rs.edu.raf.repository.KorisnikRepository;
import rs.edu.raf.repository.RadnikRepository;
import rs.edu.raf.servis.impl.KorisnikServisImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class KorisnikServisUnitTests {

    @Mock
    private KorisnikRepository korisnikRepository;

    @Mock
    private RadnikRepository radnikRepository;

    @Mock
    private KorisnikMapper korisnikMapper;

    @Mock
    private RadnikMapper radnikMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private KorisnikServisImpl korisnikServis;

    private NoviKorisnikDTO createMockNoviKorisnikDTO() {
        NoviKorisnikDTO noviKorisnikDTO = new NoviKorisnikDTO();
        noviKorisnikDTO.setIme("Pera");
        noviKorisnikDTO.setPrezime("Peric");

        noviKorisnikDTO.setJmbg(String.valueOf(1705000793457L));

        noviKorisnikDTO.setDatumRodjenja(958514400000L);
        noviKorisnikDTO.setPol("M");
        noviKorisnikDTO.setEmail("pera.petrovic@gmail.com");
        noviKorisnikDTO.setBrojTelefona("0641234567");
        noviKorisnikDTO.setAdresa("Bulevar Kralja Petra 1");
        noviKorisnikDTO.setAktivan(true);

        return noviKorisnikDTO;
    }

    private IzmenaKorisnikaDTO createMockIzmenaKorisnikDTO() {
        IzmenaKorisnikaDTO izmenaKorisnikaDTO = new IzmenaKorisnikaDTO();
        izmenaKorisnikaDTO.setPrezime("Peric");
        izmenaKorisnikaDTO.setPol("M");
        izmenaKorisnikaDTO.setEmail("pera.petrovic@gmail.com");
        izmenaKorisnikaDTO.setBrojTelefona("0641234567");
        izmenaKorisnikaDTO.setAdresa("Bulevar Kralja Petra 1");
        izmenaKorisnikaDTO.setPassword("pera123");
        izmenaKorisnikaDTO.setPovezaniRacuni("1234");
        izmenaKorisnikaDTO.setAktivan(true);

        return izmenaKorisnikaDTO;
    }

    private Korisnik createMockKorisnik(){
        Korisnik korisnik = new Korisnik();
        korisnik.setId(0L);
        korisnik.setIme("Pera");
        korisnik.setPrezime("Peric");

        korisnik.setJmbg(String.valueOf(1705000793457L));

        korisnik.setDatumRodjenja(958532400000L);
        korisnik.setPol("M");
        korisnik.setEmail("pera.petrovic@gmail.com");
        korisnik.setBrojTelefona("0641234567");
        korisnik.setAdresa("Bulevar Kralja Petra 1");
        korisnik.setPassword("pera123");
        korisnik.setPovezaniRacuni("1234");
        korisnik.setAktivan(true);

        return korisnik;
    }

    private List<Korisnik> createMockKorisnikList() {
        
        List<Korisnik> korisnici = new ArrayList<>();
        
        Korisnik korisnik1 = createMockKorisnik();
        Korisnik korisnik2 = new Korisnik();
        Korisnik korisnik3 = new Korisnik();
        
        korisnik2.setId(1L);
        korisnik2.setIme("Milos");
        korisnik2.setPrezime("Grbić");

        korisnik2.setJmbg(String.valueOf(2311000793457L));

        korisnik2.setDatumRodjenja(974952000000L);
        korisnik2.setPol("M");
        korisnik2.setEmail("milos.grbic@yahoo.com");
        korisnik2.setBrojTelefona("+381634444444");
        korisnik2.setAdresa("Nemanjina 11");
        korisnik2.setPassword("sifra1");
        korisnik2.setPovezaniRacuni("4122,3216,4126");
        korisnik2.setAktivan(true);

        korisnik3.setId(2L);
        korisnik3.setIme("Nikola");
        korisnik3.setPrezime("Zarić");

        korisnik3.setJmbg(String.valueOf(1408001793457L));

        korisnik3.setDatumRodjenja(997758000000L);
        korisnik3.setPol("M");
        korisnik3.setEmail("nikola.zaric@gmail.com");
        korisnik3.setBrojTelefona("+381601111111");
        korisnik3.setAdresa("Teodora Drajzera 8");
        korisnik3.setPassword("sifra1");
        korisnik3.setPovezaniRacuni("7988,6597,3156");
        korisnik3.setAktivan(true);
        
        korisnici.add(korisnik1);
        korisnici.add(korisnik2);
        korisnici.add(korisnik3);
        
        return korisnici;
    }

    private List<KorisnikDTO> createMockKorisnikDTOList() {

        List<KorisnikDTO> korisnici = new ArrayList<>();

        KorisnikDTO korisnik1 = new KorisnikDTO();
        KorisnikDTO korisnik2 = new KorisnikDTO();
        KorisnikDTO korisnik3 = new KorisnikDTO();

        korisnik1.setId(0L);
        korisnik1.setIme("Pera");
        korisnik1.setPrezime("Peric");

        korisnik1.setJmbg(String.valueOf(1705000793457L));

        korisnik1.setDatumRodjenja(958532400000L);
        korisnik1.setPol("M");
        korisnik1.setEmail("pera.petrovic@gmail.com");
        korisnik1.setBrojTelefona("0641234567");
        korisnik1.setAdresa("Bulevar Kralja Petra 1");
        korisnik1.setPovezaniRacuni("1234");

        korisnik2.setId(1L);
        korisnik2.setIme("Milos");
        korisnik2.setPrezime("Grbić");

        korisnik2.setJmbg(String.valueOf(2311000793457L));

        korisnik2.setDatumRodjenja(974952000000L);
        korisnik2.setPol("M");
        korisnik2.setEmail("milos.grbic@yahoo.com");
        korisnik2.setBrojTelefona("+381634444444");
        korisnik2.setAdresa("Nemanjina 11");
        korisnik2.setPovezaniRacuni("4122,3216,4126");

        korisnik3.setId(2L);
        korisnik3.setIme("Nikola");
        korisnik3.setPrezime("Zarić");

        korisnik3.setJmbg(String.valueOf(1408001793457L));

        korisnik3.setDatumRodjenja(997758000000L);
        korisnik3.setPol("M");
        korisnik3.setEmail("nikola.zaric@gmail.com");
        korisnik3.setBrojTelefona("+381601111111");
        korisnik3.setAdresa("Teodora Drajzera 8");
        korisnik3.setPovezaniRacuni("7988,6597,3156");

        korisnici.add(korisnik1);
        korisnici.add(korisnik2);
        korisnici.add(korisnik3);

        return korisnici;
    }

    private NoviRadnikDTO createMockNoviRadnikDTO() {
        NoviRadnikDTO noviRadnikDTO = new NoviRadnikDTO();
        noviRadnikDTO.setIme("Pera");
        noviRadnikDTO.setPrezime("Peric");

        noviRadnikDTO.setJmbg(String.valueOf(1705000793457L));

        noviRadnikDTO.setDatumRodjenja(958532400000L);
        noviRadnikDTO.setPol("M");
        noviRadnikDTO.setEmail("pera.petrovic@gmail.com");
        noviRadnikDTO.setBrojTelefona("0641234567");
        noviRadnikDTO.setAdresa("Bulevar Kralja Petra 1");
        noviRadnikDTO.setUsername("pera");
        noviRadnikDTO.setPassword("pera123");
        noviRadnikDTO.setPozicija("Blagajnik");
        noviRadnikDTO.setDepartman("Racunovodstvo");
        noviRadnikDTO.setPermisije(1L);
        noviRadnikDTO.setAktivan(true);

        return noviRadnikDTO;
    }

    private IzmenaRadnikaDTO createMockIzmenaRadnikDTO() {
        IzmenaRadnikaDTO izmenaRadnikaDTO = new IzmenaRadnikaDTO();
        izmenaRadnikaDTO.setPrezime("Peric");
        izmenaRadnikaDTO.setPol("M");
        izmenaRadnikaDTO.setBrojTelefona("0641234567");
        izmenaRadnikaDTO.setAdresa("Bulevar Kralja Petra 1");
        izmenaRadnikaDTO.setPassword("pera123");
        izmenaRadnikaDTO.setPozicija("Blagajnik");
        izmenaRadnikaDTO.setDepartman("Racunovodstvo");
        izmenaRadnikaDTO.setPermisije(1L);
        izmenaRadnikaDTO.setAktivan(true);

        return izmenaRadnikaDTO;
    }

    private Radnik createMockRadnik(){
        Radnik radnik = new Radnik();
        radnik.setId(0L);
        radnik.setIme("Pera");
        radnik.setPrezime("Peric");

        radnik.setJmbg(String.valueOf(1705000793457L));

        radnik.setDatumRodjenja(958532400000L);
        radnik.setPol("M");
        radnik.setEmail("pera.petrovic@gmail.com");
        radnik.setBrojTelefona("0641234567");
        radnik.setAdresa("Bulevar Kralja Petra 1");
        radnik.setUsername("pera");
        radnik.setPassword("pera123");
        radnik.setSaltPassword("salt");
        radnik.setPozicija("Blagajnik");
        radnik.setDepartman("Racunovodstvo");
        radnik.setPermisije(1L);
        radnik.setAktivan(true);

        return radnik;
    }

    private List<Radnik> createMockRadnikList() {

        List<Radnik> radnici = new ArrayList<>();

        Radnik radnik1 = createMockRadnik();
        Radnik radnik2 = new Radnik();
        Radnik radnik3 = new Radnik();

        radnik2.setId(1L);
        radnik2.setIme("Lara");
        radnik2.setPrezime("Milić");

        radnik2.setJmbg(String.valueOf(1306995793457L));

        radnik2.setDatumRodjenja(803012400000L);
        radnik2.setPol("F");
        radnik2.setEmail("lara.milic@gmail.com");
        radnik2.setBrojTelefona("+381601111111");
        radnik2.setAdresa("Francuska 5");
        radnik2.setUsername("lara");
        radnik2.setPassword("sifra1");
        radnik2.setPozicija("Blagajnik/ca");
        radnik2.setDepartman("Racunovodstvo");
        radnik2.setPermisije(1L);
        radnik2.setAktivan(true);

        radnik3.setId(2L);
        radnik3.setIme("Stevan");
        radnik3.setPrezime("Krunić");

        radnik3.setJmbg(String.valueOf(2203989793457L));

        radnik3.setDatumRodjenja(606542400000L);
        radnik3.setPol("M");
        radnik3.setEmail("stevan.krunic@gmail.com");
        radnik3.setBrojTelefona("+381601111111");
        radnik3.setAdresa("Savska 20");
        radnik3.setUsername("stevan");
        radnik3.setPassword("sifra1");
        radnik3.setPozicija("Promoter");
        radnik3.setDepartman("Finansije");
        radnik3.setPermisije(1L);
        radnik3.setAktivan(true);

        radnici.add(radnik1);
        radnici.add(radnik2);
        radnici.add(radnik3);

        return radnici;
    }

    private List<RadnikDTO> createMockRadnikDTOList() {

        List<RadnikDTO> radnici = new ArrayList<>();

        RadnikDTO radnik1 = new RadnikDTO();
        RadnikDTO radnik2 = new RadnikDTO();
        RadnikDTO radnik3 = new RadnikDTO();

        radnik1.setId(0L);
        radnik1.setIme("Pera");
        radnik1.setPrezime("Peric");

        radnik1.setJmbg(String.valueOf(1705000793457L));

        radnik1.setDatumRodjenja(958532400000L);
        radnik1.setPol("M");
        radnik1.setEmail("pera.petrovic@gmail.com");
        radnik1.setBrojTelefona("0641234567");
        radnik1.setAdresa("Bulevar Kralja Petra 1");
        radnik1.setUsername("pera");
        radnik1.setPozicija("Blagajnik");
        radnik1.setDepartman("Racunovodstvo");
        radnik1.setPermisije(1L);

        radnik2.setId(1L);
        radnik2.setIme("Lara");
        radnik2.setPrezime("Milić");

        radnik2.setJmbg(String.valueOf(1306995793457L));

        radnik2.setDatumRodjenja(803012400000L);
        radnik2.setPol("F");
        radnik2.setEmail("lara.milic@gmail.com");
        radnik2.setBrojTelefona("+381601111111");
        radnik2.setAdresa("Francuska 5");
        radnik2.setUsername("lara");
        radnik2.setPozicija("Blagajnik/ca");
        radnik2.setDepartman("Racunovodstvo");
        radnik2.setPermisije(1L);

        radnik3.setId(2L);
        radnik3.setIme("Stevan");
        radnik3.setPrezime("Krunić");

        radnik3.setJmbg(String.valueOf(2203989793457L));

        radnik3.setDatumRodjenja(606542400000L);
        radnik3.setPol("M");
        radnik3.setEmail("stevan.krunic@gmail.com");
        radnik3.setBrojTelefona("+381601111111");
        radnik3.setAdresa("Savska 20");
        radnik3.setUsername("stevan");
        radnik3.setPozicija("Promoter");
        radnik3.setDepartman("Finansije");
        radnik3.setPermisije(1L);

        radnici.add(radnik1);
        radnici.add(radnik2);
        radnici.add(radnik3);

        return radnici;
    }

    @Test
    public void testKreirajNovogKorisnika1(){

        NoviKorisnikDTO noviKorisnikDTO = createMockNoviKorisnikDTO();

        Korisnik korisnik = createMockKorisnik();

        given(korisnikRepository.save(korisnik)).willReturn(korisnik);
        given(korisnikMapper.noviKorisnikDtoToKorisnik(noviKorisnikDTO)).willReturn(korisnik);

        try{
            KorisnikDTO kreiraniKorisnik = korisnikServis.kreirajNovogKorisnika(noviKorisnikDTO);
            assertEquals(korisnikMapper.korisnikToKorisnikDto(korisnik), kreiraniKorisnik);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testKreirajNovogKorisnika2(){

        NoviKorisnikDTO noviKorisnikDTO = createMockNoviKorisnikDTO();

        Korisnik korisnik = createMockKorisnik();

        korisnik.setJmbg(String.valueOf(506995793457L));
        korisnik.setDatumRodjenja(802344392000L);

        given(korisnikRepository.save(korisnik)).willReturn(korisnik);
        given(korisnikMapper.noviKorisnikDtoToKorisnik(noviKorisnikDTO)).willReturn(korisnik);

        try{
            KorisnikDTO kreiraniKorisnik = korisnikServis.kreirajNovogKorisnika(noviKorisnikDTO);
            assertEquals(korisnikMapper.korisnikToKorisnikDto(korisnik), kreiraniKorisnik);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testKreirajNovogKorisnikaLosJMBG(){

        NoviKorisnikDTO noviKorisnikDTO = createMockNoviKorisnikDTO();

        Korisnik korisnik = createMockKorisnik();


        noviKorisnikDTO.setJmbg(String.valueOf(1605000793457L));
        korisnik.setJmbg(String.valueOf(1605000793457L));


        given(korisnikMapper.noviKorisnikDtoToKorisnik(noviKorisnikDTO)).willReturn(korisnik);

        assertThrows(Exception.class, () -> {
            korisnikServis.kreirajNovogKorisnika(noviKorisnikDTO);
        });
    }

    @Test
    public void testKreirajNovogKorisnikaLosDatumRodjenja(){

        NoviKorisnikDTO noviKorisnikDTO = createMockNoviKorisnikDTO();

        Korisnik korisnik = createMockKorisnik();

        noviKorisnikDTO.setDatumRodjenja(708532400000L);
        korisnik.setDatumRodjenja(708532400000L);

        given(korisnikMapper.noviKorisnikDtoToKorisnik(noviKorisnikDTO)).willReturn(korisnik);

        assertThrows(Exception.class, () -> {
            korisnikServis.kreirajNovogKorisnika(noviKorisnikDTO);
        });
    }

//    @Test
//    public void testRegistrujNovogKorisnika(){
//
//        RegistrujKorisnikDTO registrujKorisnikDTO = new RegistrujKorisnikDTO();
//
//        registrujKorisnikDTO.setEmail("pera.petrovic@gmail.com");
//        registrujKorisnikDTO.setBrojTelefona("0641234567");
//        registrujKorisnikDTO.setPassword("pera123");
//        registrujKorisnikDTO.setBrojRacuna("2312");
//        registrujKorisnikDTO.setCode("kod");
//
//        Korisnik korisnik = createMockKorisnik();
//
//        korisnik.setPassword("pera123");
//
//        given(korisnikRepository.save(korisnik)).willReturn(korisnik);
//        given(korisnikRepository.findByEmailAndAktivanIsTrue(registrujKorisnikDTO.getEmail())).willReturn(Optional.of(korisnik));
//
//        try{
//            KorisnikDTO kreiraniKorisnik = korisnikServis.registrujNovogKorisnika(registrujKorisnikDTO);
//            assertEquals(korisnikMapper.korisnikToKorisnikDto(korisnik), kreiraniKorisnik);
//        } catch (Exception e){
//            fail(e.getMessage());
//        }
//    }

    @Test
    public void testKreirajNovogRadnika1(){

        NoviRadnikDTO noviRadnikDTO = createMockNoviRadnikDTO();

        Radnik radnik = createMockRadnik();

        given(radnikRepository.save(radnik)).willReturn(radnik);
        given(radnikMapper.noviRadnikDtoToRadnik(noviRadnikDTO, -1L)).willReturn(radnik);

        try{
            RadnikDTO kreiraniRadnik = korisnikServis.kreirajNovogRadnika(noviRadnikDTO, -1L);
            assertEquals(radnikMapper.radnikToRadnikDto(radnik), kreiraniRadnik);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testKreirajNovogRadnika2(){

        NoviRadnikDTO noviRadnikDTO = createMockNoviRadnikDTO();

        Radnik radnik = createMockRadnik();

        radnik.setJmbg(String.valueOf(506995793457L));
        radnik.setDatumRodjenja(802344392000L);

        given(radnikRepository.save(radnik)).willReturn(radnik);
        given(radnikMapper.noviRadnikDtoToRadnik(noviRadnikDTO, -1L)).willReturn(radnik);

        try{
            RadnikDTO kreiraniRadnik = korisnikServis.kreirajNovogRadnika(noviRadnikDTO, -1L);
            assertEquals(radnikMapper.radnikToRadnikDto(radnik), kreiraniRadnik);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testKreirajNovogRadnikaLosJMBG(){

        NoviRadnikDTO noviRadnikDTO = createMockNoviRadnikDTO();

        Radnik radnik = createMockRadnik();


        noviRadnikDTO.setJmbg(String.valueOf(1605000793457L));
        radnik.setJmbg(String.valueOf(1605000793457L));


        assertThrows(Exception.class, () -> {
            korisnikServis.kreirajNovogRadnika(noviRadnikDTO, -1L);
        });
    }

    @Test
    public void testKreirajNovogRadnikaLosDatumRodjenja(){

        NoviRadnikDTO noviRadnikDTO = createMockNoviRadnikDTO();

        Radnik radnik = createMockRadnik();

        noviRadnikDTO.setDatumRodjenja(708532400000L);
        radnik.setDatumRodjenja(708532400000L);

        assertThrows(Exception.class, () -> {
            korisnikServis.kreirajNovogRadnika(noviRadnikDTO, -1L);
        });
    }

    @Test
    public void testIzmeniKorisnika1(){
        IzmenaKorisnikaDTO izmenaKorisnikaDTO = createMockIzmenaKorisnikDTO();

        Korisnik korisnik = createMockKorisnik();

        given(korisnikRepository.save(korisnik)).willReturn(korisnik);
        given(korisnikRepository.findById(izmenaKorisnikaDTO.getId())).willReturn(Optional.of(korisnik));


        try{
            KorisnikDTO izmenjenKorisnik = korisnikServis.izmeniKorisnika(izmenaKorisnikaDTO);
            assertEquals(korisnikMapper.korisnikToKorisnikDto(korisnik), izmenjenKorisnik);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testIzmeniKorisnika2(){
        IzmenaKorisnikaDTO izmenaKorisnikaDTO = createMockIzmenaKorisnikDTO();

        Korisnik korisnik = createMockKorisnik();

        korisnik.setJmbg(String.valueOf(506995793457L));
        korisnik.setDatumRodjenja(802344392000L);

        given(korisnikRepository.save(korisnik)).willReturn(korisnik);
        given(korisnikRepository.findById(izmenaKorisnikaDTO.getId())).willReturn(Optional.of(korisnik));


        try{
            KorisnikDTO izmenjenKorisnik = korisnikServis.izmeniKorisnika(izmenaKorisnikaDTO);
            assertEquals(korisnikMapper.korisnikToKorisnikDto(korisnik), izmenjenKorisnik);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testIzmeniRadnika1(){

        IzmenaRadnikaDTO izmenaRadnikaDTO = createMockIzmenaRadnikDTO();

        Radnik radnik = createMockRadnik();

        given(radnikRepository.save(radnik)).willReturn(radnik);
        given(radnikRepository.findById(izmenaRadnikaDTO.getId())).willReturn(Optional.of(radnik));

        try{
            RadnikDTO izmenjenRadnik = korisnikServis.izmeniRadnika(izmenaRadnikaDTO);
            assertEquals(radnikMapper.radnikToRadnikDto(radnik), izmenjenRadnik);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testIzmeniRadnika2(){

        IzmenaRadnikaDTO izmenaRadnikaDTO = createMockIzmenaRadnikDTO();

        Radnik radnik = createMockRadnik();

        radnik.setJmbg(String.valueOf(506995793457L));
        radnik.setDatumRodjenja(802344392000L);

        given(radnikRepository.save(radnik)).willReturn(radnik);
        given(radnikRepository.findById(izmenaRadnikaDTO.getId())).willReturn(Optional.of(radnik));

        try{
            RadnikDTO izmenjenRadnik = korisnikServis.izmeniRadnika(izmenaRadnikaDTO);
            assertEquals(radnikMapper.radnikToRadnikDto(radnik), izmenjenRadnik);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testIzlistajSveAktivneRadnike(){

        List<Radnik> radnici = createMockRadnikList();

        List<RadnikDTO> radniciDTO = createMockRadnikDTOList();

        given(radnikRepository.findAllByAktivanIsTrue()).willReturn(radnici);
        given(radnikMapper.radnikToRadnikDto(any(Radnik.class))).willReturn(radniciDTO.get(0), radniciDTO.get(1), radniciDTO.get(2));

        try{
            List<RadnikDTO> izlistaniRadniciDTO = korisnikServis.izlistajSveAktivneRadnike();
            assertEquals(radniciDTO, izlistaniRadniciDTO);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testIzlistajSveAktivneKorisnike(){

        List<Korisnik> korisnici = createMockKorisnikList();

        List<KorisnikDTO> korisniciDTO = createMockKorisnikDTOList();

        given(korisnikRepository.findAllByAktivanIsTrue()).willReturn(korisnici);
        given(korisnikMapper.korisnikToKorisnikDto(any(Korisnik.class))).willReturn(korisniciDTO.get(0), korisniciDTO.get(1), korisniciDTO.get(2));

        try{
            List<KorisnikDTO> izlistaniKorisniciDTO = korisnikServis.izlistajSveAktivneKorisnike();
            assertEquals(korisniciDTO, izlistaniKorisniciDTO);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testNadjiAktivnogRadnikaPoEmail(){
        List<Radnik> radnici = createMockRadnikList();

        List<RadnikDTO> radniciDTO = createMockRadnikDTOList();


        given(radnikRepository.findByEmailAndAktivanIsTrue(radnici.get(0).getEmail())).willReturn(Optional.of(radnici.get(0)));
        given(radnikMapper.radnikToRadnikDto(any(Radnik.class))).willReturn(radniciDTO.get(0));

        try{
            RadnikDTO izlistanRadnkDTO = korisnikServis.nadjiAktivnogRadnikaPoEmail(radnici.get(0).getEmail());
            assertEquals(radniciDTO.get(0), izlistanRadnkDTO);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testNadjiAktivnogKorisnikaPoEmail(){
        List<Korisnik> korisnici = createMockKorisnikList();

        List<KorisnikDTO> korisniciDTO = createMockKorisnikDTOList();


        given(korisnikRepository.findByEmailAndAktivanIsTrue(korisnici.get(0).getEmail())).willReturn(Optional.of(korisnici.get(0)));
        given(korisnikMapper.korisnikToKorisnikDto(any(Korisnik.class))).willReturn(korisniciDTO.get(0));

        try{
            KorisnikDTO izlistanRadnkDTO = korisnikServis.nadjiAktivnogKorisnikaPoEmail(korisnici.get(0).getEmail());
            assertEquals(korisniciDTO.get(0), izlistanRadnkDTO);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testNadjiAktivnogKorisnikaPoJMBG(){
        List<Korisnik> korisnici = createMockKorisnikList();

        List<KorisnikDTO> korisniciDTO = createMockKorisnikDTOList();


        given(korisnikRepository.findByJmbgAndAktivanIsTrue(korisnici.get(0).getJmbg())).willReturn(Optional.of(korisnici.get(0)));
        given(korisnikMapper.korisnikToKorisnikDto(any(Korisnik.class))).willReturn(korisniciDTO.get(0));

        try{
            KorisnikDTO izlistanRadnkDTO = korisnikServis.nadjiAktivnogKorisnikaPoJMBG(korisnici.get(0).getJmbg());
            assertEquals(korisniciDTO.get(0), izlistanRadnkDTO);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testNadjiAKtivnogKorisnikaPoJmbgNePostojiKorisnik() {
        when(korisnikRepository.findByJmbgAndAktivanIsTrue(anyString())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->korisnikServis.nadjiAktivnogKorisnikaPoJMBG(String.valueOf(1011000793457L)));
    }

    @Test
    public void testNadjiAktivnogKorisnikaPoBrojuTelefona(){
        List<Korisnik> korisnici = createMockKorisnikList();

        List<KorisnikDTO> korisniciDTO = createMockKorisnikDTOList();


        given(korisnikRepository.findByBrojTelefonaAndAktivanIsTrue(korisnici.get(0).getBrojTelefona())).willReturn(Optional.of(korisnici.get(0)));
        given(korisnikMapper.korisnikToKorisnikDto(any(Korisnik.class))).willReturn(korisniciDTO.get(0));

        try{
            KorisnikDTO izlistanRadnkDTO = korisnikServis.nadjiAktivnogKorisnikaPoBrojuTelefona(korisnici.get(0).getBrojTelefona());
            assertEquals(korisniciDTO.get(0), izlistanRadnkDTO);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testResetLimitRadnikNePostoji() {
        when(radnikRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->korisnikServis.resetLimit(1L,1L));
    }

    @Test
    public void testResetLimitNijeIstaFirma() {
        Radnik radnik = new Radnik();
        radnik.setFirmaId(-2L);
        radnik.setId(10L);
        when(radnikRepository.findById(anyLong())).thenReturn(Optional.of(radnik));
        assertThrows(WrongEmployeeException.class,()->korisnikServis.resetLimit(10L,-3L));
    }

    @Test
    public void testResetLimitUspesno() {
        Radnik radnik = new Radnik();
        radnik.setFirmaId(-2L);
        radnik.setId(10L);
        radnik.setDailySpent(new BigDecimal("1000.0"));
        when(radnikRepository.findById(anyLong())).thenReturn(Optional.of(radnik));
        when(radnikRepository.save(any())).thenReturn(radnik);
        korisnikServis.resetLimit(10L,-2L);
        verify(radnikRepository,times(1)).save(any());
    }

    @Test
    public void testAddAcountNemaKorisnika() {
        when(korisnikRepository.findKorisnikByIdAndAktivanIsTrue(anyLong())).thenReturn(Optional.empty());
        assertFalse(korisnikServis.addAccountToUser(1L,1L));
    }

    @Test
    public void testAddAcountNemaRacune() {
        Korisnik korisnik = new Korisnik();
        korisnik.setAktivan(true);
        korisnik.setId(100L);
        when(korisnikRepository.findKorisnikByIdAndAktivanIsTrue(anyLong())).thenReturn(Optional.of(korisnik));
        when(korisnikRepository.save(any())).thenReturn(korisnik);
        assertTrue(korisnikServis.addAccountToUser(100L,1L));
    }

    @Test
    public void testAddAcountVecImaRacun() {
        Korisnik korisnik = new Korisnik();
        korisnik.setAktivan(true);
        korisnik.setId(100L);
        korisnik.setPovezaniRacuni(String.valueOf(1L));
        when(korisnikRepository.findKorisnikByIdAndAktivanIsTrue(anyLong())).thenReturn(Optional.of(korisnik));
//        when(korisnikRepository.save(any())).thenReturn(korisnik);
        assertFalse(korisnikServis.addAccountToUser(100L,1L));
    }

    @Test
    public void testAddAcountUspesno() {
        Korisnik korisnik = new Korisnik();
        korisnik.setAktivan(true);
        korisnik.setId(100L);
        korisnik.setPovezaniRacuni(String.valueOf(2L));
        when(korisnikRepository.findKorisnikByIdAndAktivanIsTrue(anyLong())).thenReturn(Optional.of(korisnik));
        when(korisnikRepository.save(any())).thenReturn(korisnik);
        assertTrue(korisnikServis.addAccountToUser(100L,1L));
    }
}
