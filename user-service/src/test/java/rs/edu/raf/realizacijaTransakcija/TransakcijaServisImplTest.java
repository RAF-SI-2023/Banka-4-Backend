package rs.edu.raf.realizacijaTransakcija;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import rs.edu.raf.racun.model.DevizniRacun;
import rs.edu.raf.racun.model.PravniRacun;
import rs.edu.raf.racun.model.TekuciRacun;
import rs.edu.raf.racun.servis.RacunServis;
import rs.edu.raf.transakcija.dto.RealizacijaTransakcije;
import rs.edu.raf.transakcija.mapper.DtoOriginalMapper;
import rs.edu.raf.transakcija.model.PrenosSredstava;
import rs.edu.raf.transakcija.model.Status;
import rs.edu.raf.transakcija.model.Uplata;
import rs.edu.raf.transakcija.repository.*;
import rs.edu.raf.transakcija.servis.impl.TransakcijaServisImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransakcijaServisImplTest {

    @Mock
    private RacunServis racunServis;

    @Mock
    private UplataRepository uplataRepository;

    @Mock
    private PrenosSredstavaRepository prenosSredstavaRepository;

    @Mock
    private SablonTransakcijeRepository sablonTransakcijeRepository;

    @Mock
    private PravniRacunRepository pravniRacunRepository;

    @Mock
    private TekuciRacunRepository tekuciRacunRepository;

    @Mock
    private DevizniRacunRepository devizniRacunRepository;

    @Mock
    private DtoOriginalMapper dtoOriginalMapper;

    private TransakcijaServisImpl transakcijaServis;

    private TekuciRacun tekuciRacun;

    private PravniRacun pravniRacun;

    private DevizniRacun devizniRacun;

    private PrenosSredstava prenosSredstava;

    private Uplata uplata;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        transakcijaServis = Mockito.spy(new TransakcijaServisImpl(
                uplataRepository,
                prenosSredstavaRepository,
                sablonTransakcijeRepository,
                pravniRacunRepository,
                tekuciRacunRepository,
                devizniRacunRepository,
                dtoOriginalMapper,
                racunServis
        ));
    }

    private void initObject() {
        tekuciRacun = new TekuciRacun(
                987654321L,// Broj računa
                456L,          // ID vlasnika
                new BigDecimal("1000.00"),   // Stanje
                new BigDecimal("900.00"),    // Raspoloživo stanje
                789L,          // ID zaposlenog
                1234567890L,   // Datum kreiranja
                1234567890L,   // Datum isteka
                "EUR",         // Valuta
                true,          // Aktivan
                "Tekući",      // Vrsta računa
                new BigDecimal("0.05"),      // Kamatna stopa
                new BigDecimal("10.00")      // Održavanje računa
        );

        pravniRacun = new PravniRacun(
                1L, // ID
                987654321L, // Broj računa
                456L,       // ID Firme
                new BigDecimal("1000.00"),   // Stanje
                new BigDecimal("900.00"),    // Raspoloživo stanje
                789L,       // ID zaposlenog
                1234567890L,   // Datum kreiranja
                1234567890L,   // Datum isteka
                "EUR",     // Valuta
                true      // Aktivan
        );

        devizniRacun = new DevizniRacun(
                987654321L, // Broj računa
                456L,       // ID vlasnika
                new BigDecimal("1000.00"),   // Stanje
                new BigDecimal("900.00"),    // Raspoloživo stanje
                789L,       // ID zaposlenog
                1234567890L,   // Datum kreiranja
                1234567890L,   // Datum isteka
                "EUR",     // Valuta
                "EUR",     // Default valuta
                true,      // Aktivan
                new BigDecimal("0.05"),      // Kamatna stopa
                new BigDecimal("10.00"),      // Održavanje računa
                5          // Broj dozvoljenih valuta
        );

        prenosSredstava = new PrenosSredstava(
                1L,                 // ID
                123456L,            // RacunPosiljaoca
                789012L,            // RacunPrimaoca
                new BigDecimal("50.00"),  // Iznos
                1647162000L,        // Vreme (na primer: 15. mart 2022, 12:00)
                Status.U_OBRADI,    // Status
                null                // VremeIzvrsavanja (može biti null ako nije izvršen)
        );

        uplata = new Uplata(
                1L, // ID
                123456L, // Broj računa pošiljaoca
                "Ime Primaoca", // Naziv primaoca
                789012L, // Broj računa primaoca
                new BigDecimal("100.00"), // Iznos uplate
                1234, // Poziv na broj
                5678, // Šifra plaćanja
                "Svrha Plaćanja", // Svrha plaćanja
                Status.U_OBRADI, // Status uplate
                System.currentTimeMillis(), // Vreme transakcije
                null // VremeIzvrsavanja (može biti null ako nije izvršen)
        );
    }


    /////////// ProveriZajednickiElement metoda
    @Test
    public void shouldReturnTrueWhenCommonElementExists() {
        String[] array1 = {"USD", "EUR", "GBP"};
        String[] array2 = {"JPY", "EUR", "CAD"};
        assertTrue(transakcijaServis.proveriZajednickiElement(array1, array2));
    }

    @Test
    public void shouldReturnFalseWhenNoCommonElementExists() {
        String[] array1 = {"USD", "GBP"};
        String[] array2 = {"JPY", "CAD"};
        assertFalse(transakcijaServis.proveriZajednickiElement(array1, array2));
    }

    @Test
    public void shouldReturnFalseWhenOneArrayIsEmpty() {
        String[] array1 = {};
        String[] array2 = {"JPY", "CAD"};
        assertFalse(transakcijaServis.proveriZajednickiElement(array1, array2));
    }

    @Test
    public void shouldReturnFalseWhenBothArraysAreEmpty() {
        String[] array1 = {};
        String[] array2 = {};
        assertFalse(transakcijaServis.proveriZajednickiElement(array1, array2));
    }

    /////////// NeuspeoPrenos metoda

    //----- Tekuci switch case -----//
    @Test
    public void shouldHandleFailedTransferForTekuciRacun() {
        initObject();

        long vremeIzvrsavanja = System.currentTimeMillis();

        PrenosSredstava promenjenPrenosSredstava = new PrenosSredstava(
                1L,                 // ID
                123456L,            // RacunPosiljaoca
                789012L,            // RacunPrimaoca
                new BigDecimal("50.00"),  // Iznos
                1647162000L,        // Vreme (na primer: 15. mart 2022, 12:00)
                Status.NEUSPELO,           // Status
                vremeIzvrsavanja);

        Mockito.doReturn(tekuciRacun).when(racunServis).nadjiAktivanTekuciRacunPoBrojuRacuna(Mockito.anyLong());  // vrati racun

        Mockito.doReturn(promenjenPrenosSredstava).when(transakcijaServis).promeniStatusPrenosaSredstava(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());

        transakcijaServis.neuspeoPrenos("TekuciRacun", prenosSredstava);

        assertEquals(new BigDecimal("950.00"), tekuciRacun.getRaspolozivoStanje());  // provera da li se vratio novac
    }

    @Test
    public void shouldHandleFailedTransferForNonExistentTekuciRacun() {
        initObject();
        Mockito.when(racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(Mockito.anyLong())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspeoPrenos("TekuciRacun", prenosSredstava));
    }

    @Test
    public void shouldHandleFailedTransferForTekuciRacunWithNullRacunPosiljaoca() {
        prenosSredstava = new PrenosSredstava(
                1L,                 // ID
                null,            // RacunPosiljaoca
                789012L,            // RacunPrimaoca
                new BigDecimal("50.00"),  // Iznos
                1647162000L,        // Vreme (na primer: 15. mart 2022, 12:00)
                Status.U_OBRADI,    // Status
                null                // VremeIzvrsavanja (može biti null ako nije izvršen)
        );

        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspeoPrenos("TekuciRacun", prenosSredstava));
    }


    //----- Pravni switch case -----//
    @Test
    public void shouldHandleFailedTransferForPravniRacun() {
        initObject();

        long vremeIzvrsavanja = System.currentTimeMillis();

        PrenosSredstava promenjenPrenosSredstava = new PrenosSredstava(
                1L,                 // ID
                123456L,            // RacunPosiljaoca
                789012L,            // RacunPrimaoca
                new BigDecimal("50.00"),  // Iznos
                1647162000L,        // Vreme (na primer: 15. mart 2022, 12:00)
                Status.NEUSPELO,           // Status
                vremeIzvrsavanja);

        Mockito.doReturn(promenjenPrenosSredstava).when(transakcijaServis).promeniStatusPrenosaSredstava(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());

        Mockito.doReturn(pravniRacun).when(racunServis).nadjiAktivanPravniRacunPoBrojuRacuna(Mockito.anyLong());

        transakcijaServis.neuspeoPrenos("PravniRacun", prenosSredstava);

        assertEquals(new BigDecimal("950.00"), pravniRacun.getRaspolozivoStanje());  // provera da li se vratio novac
    }

    @Test
    public void shouldHandleFailedTransferForNonExistentPravniRacun() {
        initObject();
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(Mockito.anyLong())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspeoPrenos("PravniRacun", prenosSredstava));
    }

    @Test
    public void shouldHandleFailedTransferForPravniRacunWithNullRacunPosiljaoca() {
        prenosSredstava = new PrenosSredstava(
                1L,                 // ID
                null,            // RacunPosiljaoca
                789012L,            // RacunPrimaoca
                new BigDecimal("50.00"),  // Iznos
                1647162000L,        // Vreme (na primer: 15. mart 2022, 12:00)
                Status.U_OBRADI,    // Status
                null                // VremeIzvrsavanja (može biti null ako nije izvršen)
        );

        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspeoPrenos("PravniRacun", prenosSredstava));
    }


    //----- Devizni switch case -----//
    @Test
    public void shouldHandleFailedTransferForDevizniRacun() {
        initObject();

        long vremeIzvrsavanja = System.currentTimeMillis();

        PrenosSredstava promenjenPrenosSredstava = new PrenosSredstava(
                1L,                 // ID
                123456L,            // RacunPosiljaoca
                789012L,            // RacunPrimaoca
                new BigDecimal("50.00"),  // Iznos
                1647162000L,        // Vreme (na primer: 15. mart 2022, 12:00)
                Status.NEUSPELO,           // Status
                vremeIzvrsavanja);

        Mockito.doReturn(promenjenPrenosSredstava).when(transakcijaServis).promeniStatusPrenosaSredstava(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());

        Mockito.doReturn(devizniRacun).when(racunServis).nadjiAktivanDevizniRacunPoBrojuRacuna(Mockito.anyLong());

        transakcijaServis.neuspeoPrenos("DevizniRacun", prenosSredstava);

        assertEquals(new BigDecimal("950.00"), devizniRacun.getRaspolozivoStanje());  // provera da li se vratio novac

    }

    @Test
    public void shouldHandleFailedTransferForNonExistentDevizniRacun() {
        initObject();
        Mockito.when(racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(Mockito.anyLong())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspeoPrenos("DevizniRacun", prenosSredstava));
    }

    @Test
    public void shouldHandleFailedTransferForDevizniRacunWithNullRacunPosiljaoca() {
        prenosSredstava = new PrenosSredstava(
                1L,                 // ID
                null,            // RacunPosiljaoca
                789012L,            // RacunPrimaoca
                new BigDecimal("50.00"),  // Iznos
                1647162000L,        // Vreme (na primer: 15. mart 2022, 12:00)
                Status.U_OBRADI,    // Status
                null                // VremeIzvrsavanja (može biti null ako nije izvršen)
        );

        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspeoPrenos("DevizniRacun", prenosSredstava));
    }


    /////////// NeusplaUplata metoda

    //----- Tekuci switch case -----//
    @Test
    public void shouldHandleFailedPaymentForTekuciRacun() {
        initObject();

        long vremeIzvrsavanja = System.currentTimeMillis() + 1000;

        Uplata promenjenaUplata = new Uplata(
                1L, // ID
                123456L, // Broj računa pošiljaoca
                "Ime Primaoca", // Naziv primaoca
                789012L, // Broj računa primaoca
                new BigDecimal("100.00"), // Iznos uplate
                1234, // Poziv na broj
                5678, // Šifra plaćanja
                "Svrha Plaćanja", // Svrha plaćanja
                Status.NEUSPELO, // Status uplate
                System.currentTimeMillis(), // Vreme transakcije
                vremeIzvrsavanja // VremeIzvrsavanja (može biti null ako nije izvršen)
        );

        Mockito.doReturn(tekuciRacun).when(racunServis).nadjiAktivanTekuciRacunPoBrojuRacuna(Mockito.anyLong());  // vrati racun

        Mockito.doReturn(promenjenaUplata).when(transakcijaServis).promeniStatusUplate(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());

        transakcijaServis.neuspelaUplata("TekuciRacun", uplata);

        assertEquals(new BigDecimal("1000.00"), tekuciRacun.getRaspolozivoStanje());  // provera da li se vratio novac
    }

    @Test
    public void shouldHandleFailedPaymentForNonExistentTekuciRacun() {
        initObject();
        Mockito.when(racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(Mockito.anyLong())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspelaUplata("TekuciRacun", uplata));
    }

    @Test
    public void shouldHandleFailedPaymentForTekuciRacunWithNullRacunPosiljaoca() {
        uplata = new Uplata(
                1L, // ID
                null, // Broj računa pošiljaoca
                "Ime Primaoca", // Naziv primaoca
                789012L, // Broj računa primaoca
                new BigDecimal("100.00"), // Iznos uplate
                1234, // Poziv na broj
                5678, // Šifra plaćanja
                "Svrha Plaćanja", // Svrha plaćanja
                Status.U_OBRADI, // Status uplate
                System.currentTimeMillis(), // Vreme transakcije
                null // VremeIzvrsavanja (može biti null ako nije izvršen)
        );

        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspelaUplata("TekuciRacun", uplata));
    }


    //----- Pravni switch case -----//
    @Test
    public void shouldHandleFailedPaymentForPravniRacun() {
        initObject();

        long vremeIzvrsavanja = System.currentTimeMillis() + 1000;

        Uplata promenjenaUplata = new Uplata(
                1L, // ID
                123456L, // Broj računa pošiljaoca
                "Ime Primaoca", // Naziv primaoca
                789012L, // Broj računa primaoca
                new BigDecimal("100.00"), // Iznos uplate
                1234, // Poziv na broj
                5678, // Šifra plaćanja
                "Svrha Plaćanja", // Svrha plaćanja
                Status.NEUSPELO, // Status uplate
                System.currentTimeMillis(), // Vreme transakcije
                vremeIzvrsavanja // VremeIzvrsavanja (može biti null ako nije izvršen)
        );

        Mockito.doReturn(promenjenaUplata).when(transakcijaServis).promeniStatusUplate(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());

        Mockito.doReturn(pravniRacun).when(racunServis).nadjiAktivanPravniRacunPoBrojuRacuna(Mockito.anyLong());

        transakcijaServis.neuspelaUplata("PravniRacun", uplata);

        assertEquals(new BigDecimal("1000.00"), pravniRacun.getRaspolozivoStanje());  // provera da li se vratio novac
    }

    @Test
    public void shouldHandleFailedPaymentForNonExistentPravniRacun() {
        initObject();
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(Mockito.anyLong())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspelaUplata("PravniRacun", uplata));
    }

    @Test
    public void shouldHandleFailedPaymentForPravniRacunWithNullRacunPosiljaoca() {
        uplata = new Uplata(
                1L, // ID
                null, // Broj računa pošiljaoca
                "Ime Primaoca", // Naziv primaoca
                789012L, // Broj računa primaoca
                new BigDecimal("100.00"), // Iznos uplate
                1234, // Poziv na broj
                5678, // Šifra plaćanja
                "Svrha Plaćanja", // Svrha plaćanja
                Status.U_OBRADI, // Status uplate
                System.currentTimeMillis(), // Vreme transakcije
                null // VremeIzvrsavanja (može biti null ako nije izvršen)
        );

        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspelaUplata("PravniRacun", uplata));
    }


    //----- Devizni switch case -----//
    @Test
    public void shouldHandleFailedPaymentForDevizniRacun() {
        initObject();

        long vremeIzvrsavanja = System.currentTimeMillis() + 1000;

        Uplata promenjenaUplata = new Uplata(
                1L, // ID
                123456L, // Broj računa pošiljaoca
                "Ime Primaoca", // Naziv primaoca
                789012L, // Broj računa primaoca
                new BigDecimal("100.00"), // Iznos uplate
                1234, // Poziv na broj
                5678, // Šifra plaćanja
                "Svrha Plaćanja", // Svrha plaćanja
                Status.NEUSPELO, // Status uplate
                System.currentTimeMillis(), // Vreme transakcije
                vremeIzvrsavanja // VremeIzvrsavanja (može biti null ako nije izvršen)
        );

        Mockito.doReturn(promenjenaUplata).when(transakcijaServis).promeniStatusUplate(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());

        Mockito.doReturn(devizniRacun).when(racunServis).nadjiAktivanDevizniRacunPoBrojuRacuna(Mockito.anyLong());

        transakcijaServis.neuspelaUplata("DevizniRacun", uplata);

        assertEquals(new BigDecimal("1000.00"), devizniRacun.getRaspolozivoStanje());  // provera da li se vratio novac
    }

    @Test
    public void shouldHandleFailedPaymentForNonExistentDevizniRacun() {
        initObject();
        Mockito.when(racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(Mockito.anyLong())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspelaUplata("DevizniRacun", uplata));
    }

    @Test
    public void shouldHandleFailedPaymentForDevizniRacunWithNullRacunPosiljaoca() {
        uplata = new Uplata(
                1L, // ID
                null, // Broj računa pošiljaoca
                "Ime Primaoca", // Naziv primaoca
                789012L, // Broj računa primaoca
                new BigDecimal("100.00"), // Iznos uplate
                1234, // Poziv na broj
                5678, // Šifra plaćanja
                "Svrha Plaćanja", // Svrha plaćanja
                Status.NEUSPELO, // Status uplate
                System.currentTimeMillis(), // Vreme transakcije
                null // VremeIzvrsavanja (može biti null ako nije izvršen)
        );

        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspelaUplata("DevizniRacun", uplata));
    }


    /////////// realizacijaTransakcijaZaUplatu metoda

    //----- Pravni switch case -----//
    @Test
    public void shouldNotExecuteTransactionsForPaymentWhenPosiljalacNotFoundPravniRacun() {
        initObject();

        List<Uplata> uplate = List.of(uplata);
        uplata.setRacunPrimaoca(null);

        Mockito.doReturn(uplate).when(transakcijaServis).vratiUplateUObradi();

        Mockito.when(racunServis.nadjiVrstuRacuna(uplata.getRacunPosiljaoca())).thenReturn("PravniRacun");
        Mockito.when(racunServis.nadjiVrstuRacuna(uplata.getRacunPrimaoca())).thenReturn("NepostojecaVrstaRacuna");
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(Mockito.anyLong())).thenReturn(pravniRacun);

        Mockito.doReturn(new BigDecimal("100.00")).when(transakcijaServis).izracunajRezervisanaSredstva(pravniRacun.getId());
        Mockito.doReturn(uplata).when(transakcijaServis).promeniStatusUplate(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());

        transakcijaServis.realizacijaTransakcijaZaUplatu();

        Mockito.verify(transakcijaServis, Mockito.times(1)).promeniStatusUplate(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());
    }

    @Test
    public void shouldNotExecuteTransactionsForPaymentWhenPrimaocNotFoundPravniRacun() {
        initObject();

        List<Uplata> uplate = List.of(uplata);
        uplata.setRacunPosiljaoca(null);

        Mockito.doReturn(uplate).when(transakcijaServis).vratiUplateUObradi();

        Mockito.when(racunServis.nadjiVrstuRacuna(uplata.getRacunPosiljaoca())).thenReturn("NepostojecaVrstaRacuna");
        Mockito.when(racunServis.nadjiVrstuRacuna(uplata.getRacunPrimaoca())).thenReturn("PravniRacun");
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(Mockito.anyLong())).thenReturn(pravniRacun);

        Mockito.doReturn(new BigDecimal("100.00")).when(transakcijaServis).izracunajRezervisanaSredstva(pravniRacun.getId());
        Mockito.doReturn(uplata).when(transakcijaServis).promeniStatusUplate(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());

        transakcijaServis.realizacijaTransakcijaZaUplatu();

        Mockito.verify(transakcijaServis, Mockito.times(1)).promeniStatusUplate(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());
    }

    @Test
    public void shouldNotExecuteTransactionsForPaymentWhenIsNotActivePravniRacunPosiljaoc() {
        initObject();
        pravniRacun.setAktivan(false);

        PravniRacun racunPrimaoca = new PravniRacun(
                2L, // ID
                987654321L, // Broj računa
                456L,       // ID Firme
                new BigDecimal("1000.00"),   // Stanje
                new BigDecimal("900.00"),    // Raspoloživo stanje
                789L,       // ID zaposlenog
                1234567890L,   // Datum kreiranja
                1234567890L,   // Datum isteka
                "EUR",     // Valuta
                true      // Aktivan
        );

        List<Uplata> uplate = List.of(uplata);

        Mockito.doReturn(uplate).when(transakcijaServis).vratiUplateUObradi();

        Mockito.when(racunServis.nadjiVrstuRacuna(Mockito.anyLong())).thenReturn("PravniRacun");
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(uplata.getRacunPosiljaoca())).thenReturn(pravniRacun);
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(uplata.getRacunPrimaoca())).thenReturn(racunPrimaoca);

        Mockito.doReturn(new BigDecimal("100.00")).when(transakcijaServis).izracunajRezervisanaSredstva(pravniRacun.getId());
        Mockito.doReturn(new BigDecimal("100.00")).when(transakcijaServis).izracunajRezervisanaSredstva(racunPrimaoca.getId());

        Mockito.doNothing().when(transakcijaServis).neuspelaUplata(Mockito.anyString(), Mockito.any(Uplata.class));

        transakcijaServis.realizacijaTransakcijaZaUplatu();

        Mockito.verify(transakcijaServis, Mockito.times(0)).promeniStatusUplate(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(transakcijaServis, Mockito.times(1)).neuspelaUplata(Mockito.anyString(), Mockito.any(Uplata.class));
    }

    @Test
    public void shouldNotExecuteTransactionsForPaymentWhenIsNotActivePravniRacunPrimaoc() {
        initObject();

        PravniRacun racunPrimaoca = new PravniRacun(
                2L, // ID
                987654321L, // Broj računa
                456L,       // ID Firme
                new BigDecimal("1000.00"),   // Stanje
                new BigDecimal("900.00"),    // Raspoloživo stanje
                789L,       // ID zaposlenog
                1234567890L,   // Datum kreiranja
                1234567890L,   // Datum isteka
                "EUR",     // Valuta
                false      // Aktivan
        );

        List<Uplata> uplate = List.of(uplata);

        Mockito.doReturn(uplate).when(transakcijaServis).vratiUplateUObradi();

        Mockito.when(racunServis.nadjiVrstuRacuna(Mockito.anyLong())).thenReturn("PravniRacun");
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(uplata.getRacunPosiljaoca())).thenReturn(pravniRacun);
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(uplata.getRacunPrimaoca())).thenReturn(racunPrimaoca);

        Mockito.doReturn(new BigDecimal("100.00")).when(transakcijaServis).izracunajRezervisanaSredstva(pravniRacun.getId());
        Mockito.doReturn(new BigDecimal("100.00")).when(transakcijaServis).izracunajRezervisanaSredstva(racunPrimaoca.getId());

        Mockito.doNothing().when(transakcijaServis).neuspelaUplata(Mockito.anyString(), Mockito.any(Uplata.class));

        transakcijaServis.realizacijaTransakcijaZaUplatu();

        Mockito.verify(transakcijaServis, Mockito.times(0)).promeniStatusUplate(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(transakcijaServis, Mockito.times(1)).neuspelaUplata(Mockito.anyString(), Mockito.any(Uplata.class));
    }

    @Test
    public void shouldNotExecuteTransactionsForPaymentWhenIsCurrencyNotFoundPravniRacun() {
        initObject();

        PravniRacun racunPrimaoca = new PravniRacun(
                2L, // ID
                987654321L, // Broj računa
                456L,       // ID Firme
                new BigDecimal("1000.00"),   // Stanje
                new BigDecimal("900.00"),    // Raspoloživo stanje
                789L,       // ID zaposlenog
                1234567890L,   // Datum kreiranja
                1234567890L,   // Datum isteka
                "USD,RSD",     // Valuta
                true      // Aktivan
        );

        List<Uplata> uplate = List.of(uplata);

        Mockito.doReturn(uplate).when(transakcijaServis).vratiUplateUObradi();

        Mockito.when(racunServis.nadjiVrstuRacuna(Mockito.anyLong())).thenReturn("PravniRacun");
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(uplata.getRacunPosiljaoca())).thenReturn(pravniRacun);
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(uplata.getRacunPrimaoca())).thenReturn(racunPrimaoca);

        Mockito.doReturn(new BigDecimal("100.00")).when(transakcijaServis).izracunajRezervisanaSredstva(pravniRacun.getId());
        Mockito.doReturn(new BigDecimal("100.00")).when(transakcijaServis).izracunajRezervisanaSredstva(racunPrimaoca.getId());
        Mockito.doReturn(false).when(transakcijaServis).proveriZajednickiElement(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(transakcijaServis).neuspelaUplata(Mockito.anyString(), Mockito.any(Uplata.class));

        transakcijaServis.realizacijaTransakcijaZaUplatu();

        Mockito.verify(transakcijaServis, Mockito.times(0)).promeniStatusUplate(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(transakcijaServis, Mockito.times(1)).neuspelaUplata(Mockito.anyString(), Mockito.any(Uplata.class));
    }

    @Test
    public void shouldNotExecuteTransactionsForPaymentWhenNotEnoughMoneyPravniRacun() {
        initObject();

        PravniRacun racunPrimaoca = new PravniRacun(
                2L, // ID
                987654321L, // Broj računa
                456L,       // ID Firme
                new BigDecimal("1000.00"),   // Stanje
                new BigDecimal("900.00"),    // Raspoloživo stanje
                789L,       // ID zaposlenog
                1234567890L,   // Datum kreiranja
                1234567890L,   // Datum isteka
                "EUR",     // Valuta
                true      // Aktivan
        );

        List<Uplata> uplate = List.of(uplata);
        uplata.setIznos(new BigDecimal("300.00"));

        Mockito.doReturn(uplate).when(transakcijaServis).vratiUplateUObradi();

        Mockito.when(racunServis.nadjiVrstuRacuna(Mockito.anyLong())).thenReturn("PravniRacun");
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(uplata.getRacunPosiljaoca())).thenReturn(pravniRacun);
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(uplata.getRacunPrimaoca())).thenReturn(racunPrimaoca);

        Mockito.doReturn(new BigDecimal("100.00")).when(transakcijaServis).izracunajRezervisanaSredstva(pravniRacun.getId());
        Mockito.doReturn(new BigDecimal("100.00")).when(transakcijaServis).izracunajRezervisanaSredstva(racunPrimaoca.getId());
        Mockito.doReturn(true).when(transakcijaServis).proveriZajednickiElement(Mockito.any(), Mockito.any());
        Mockito.doReturn(uplata).when(transakcijaServis).promeniStatusUplate(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());


        transakcijaServis.realizacijaTransakcijaZaUplatu();

        Mockito.verify(transakcijaServis, Mockito.times(1)).promeniStatusUplate(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());
        Mockito.verify(transakcijaServis, Mockito.times(0)).neuspelaUplata(Mockito.anyString(), Mockito.any(Uplata.class));
    }


    @Test
    public void shouldExecuteTransactionsForPaymentPravniRacun() {
        initObject();

        PravniRacun racunPrimaoca = new PravniRacun(
                2L, // ID
                9876543210L, // Broj računa
                456L,       // ID Firme
                new BigDecimal("1000.00"),   // Stanje
                new BigDecimal("900.00"),    // Raspoloživo stanje
                789L,       // ID zaposlenog
                1234567890L,   // Datum kreiranja
                1234567890L,   // Datum isteka
                "EUR",     // Valuta
                true      // Aktivan
        );



        PravniRacun promenjenRacunPosiljaoca = new PravniRacun(
                1L, // ID
                987654321L, // Broj računa
                456L,       // ID Firme
                new BigDecimal("900.00"),   // Stanje
                new BigDecimal("900.00"),    // Raspoloživo stanje
                789L,       // ID zaposlenog
                1234567890L,   // Datum kreiranja
                1234567890L,   // Datum isteka
                "EUR",     // Valuta
                true      // Aktivan
        );

        PravniRacun promenjenRacunPrimaoca = new PravniRacun(
                2L, // ID
                9876543210L, // Broj računa
                456L,       // ID Firme
                new BigDecimal("1100.00"),   // Stanje
                new BigDecimal("1000.00"),    // Raspoloživo stanje
                789L,       // ID zaposlenog
                1234567890L,   // Datum kreiranja
                1234567890L,   // Datum isteka
                "EUR",     // Valuta
                true      // Aktivan
        );



        List<Uplata> uplate = List.of(uplata);

        Mockito.doReturn(uplate).when(transakcijaServis).vratiUplateUObradi();

        Mockito.when(racunServis.nadjiVrstuRacuna(Mockito.anyLong())).thenReturn("PravniRacun");
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(uplata.getRacunPosiljaoca())).thenReturn(pravniRacun);
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(uplata.getRacunPrimaoca())).thenReturn(racunPrimaoca);

        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(pravniRacun.getBrojRacuna())).thenReturn(pravniRacun);
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(racunPrimaoca.getBrojRacuna())).thenReturn(racunPrimaoca);
        Mockito.when(pravniRacunRepository.save(pravniRacun)).thenReturn(promenjenRacunPosiljaoca);
        Mockito.when(pravniRacunRepository.save(racunPrimaoca)).thenReturn(promenjenRacunPrimaoca);


        Mockito.doReturn(new BigDecimal("100.00")).when(transakcijaServis).izracunajRezervisanaSredstva(pravniRacun.getId());
        Mockito.doReturn(new BigDecimal("100.00")).when(transakcijaServis).izracunajRezervisanaSredstva(racunPrimaoca.getId());
        Mockito.doReturn(true).when(transakcijaServis).proveriZajednickiElement(Mockito.any(), Mockito.any());
        Mockito.doReturn(uplata).when(transakcijaServis).promeniStatusUplate(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());


        transakcijaServis.realizacijaTransakcijaZaUplatu();

//        Mockito.verify(transakcijaServis, Mockito.times(0)).neuspelaUplata(Mockito.anyString(), Mockito.any(Uplata.class));
        Mockito.verify(transakcijaServis, Mockito.times(1)).promeniStatusUplate(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());
       assertEquals(new BigDecimal("900.00"), pravniRacun.getStanje());
       assertEquals(new BigDecimal("1100.00"), racunPrimaoca.getStanje());


    }

}