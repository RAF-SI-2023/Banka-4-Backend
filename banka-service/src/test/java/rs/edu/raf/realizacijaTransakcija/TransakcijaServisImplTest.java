package rs.edu.raf.realizacijaTransakcija;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import rs.edu.raf.model.entities.racun.DevizniRacun;
import rs.edu.raf.model.entities.racun.PravniRacun;
import rs.edu.raf.model.entities.racun.TekuciRacun;
import rs.edu.raf.repository.transaction.*;
import rs.edu.raf.service.ExchangeRateServiceImpl;
import rs.edu.raf.service.racun.RacunServis;
import rs.edu.raf.model.entities.transaction.PrenosSredstava;
import rs.edu.raf.model.entities.transaction.Status;
import rs.edu.raf.model.entities.transaction.Uplata;
import rs.edu.raf.service.transaction.impl.TransakcijaServisImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Disabled
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


    private TransakcijaServisImpl transakcijaServis;

    private TekuciRacun tekuciRacun;

    private PravniRacun pravniRacun;

    private DevizniRacun devizniRacun;

    private PrenosSredstava prenosSredstava;
    private ExchangeRateServiceImpl exchangeRateServiceImpl;

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
                exchangeRateServiceImpl,
                racunServis,
                Mockito.mock(SimpMessagingTemplate.class)
        ));
    }

    private void initObject(){
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
                "1",                 // ID
                123456L,            // RacunPosiljaoca
                789012L,            // RacunPrimaoca
                new BigDecimal("50.00"),  // Iznos
                1647162000L,        // Vreme (na primer: 15. mart 2022, 12:00)
                Status.U_OBRADI,    // Status
                null                // VremeIzvrsavanja (može biti null ako nije izvršen)
        );

        uplata = new Uplata(
                "1", // ID
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
                "1",                 // ID
                123456L,            // RacunPosiljaoca
                789012L,            // RacunPrimaoca
                new BigDecimal("50.00"),  // Iznos
                1647162000L,        // Vreme (na primer: 15. mart 2022, 12:00)
                Status.NEUSPELO,           // Status
                vremeIzvrsavanja);

        Mockito.doReturn(tekuciRacun).when(racunServis).nadjiAktivanTekuciRacunPoBrojuRacuna(Mockito.anyLong());  // vrati racun

        Mockito.doReturn(promenjenPrenosSredstava).when(transakcijaServis).promeniStatusPrenosaSredstava(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());

        transakcijaServis.neuspeoPrenos("TekuciRacun", prenosSredstava);

        assertEquals(new BigDecimal("950.00"),tekuciRacun.getRaspolozivoStanje());  // provera da li se vratio novac
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
                "1",                 // ID
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
    public void shouldHandleFailedTransferForPravniRacun(){
        initObject();

        long vremeIzvrsavanja = System.currentTimeMillis();

        PrenosSredstava promenjenPrenosSredstava = new PrenosSredstava(
                "1",                 // ID
                123456L,            // RacunPosiljaoca
                789012L,            // RacunPrimaoca
                new BigDecimal("50.00"),  // Iznos
                1647162000L,        // Vreme (na primer: 15. mart 2022, 12:00)
                Status.NEUSPELO,           // Status
                vremeIzvrsavanja);

        Mockito.doReturn(promenjenPrenosSredstava).when(transakcijaServis).promeniStatusPrenosaSredstava(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());

        Mockito.doReturn(pravniRacun).when(racunServis).nadjiAktivanPravniRacunPoBrojuRacuna(Mockito.anyLong());

        transakcijaServis.neuspeoPrenos("PravniRacun", prenosSredstava);

        assertEquals(new BigDecimal("950.00"),pravniRacun.getRaspolozivoStanje());  // provera da li se vratio novac
    }

    @Test
    public void shouldHandleFailedTransferForNonExistentPravniRacun() {
        initObject();
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(Mockito.anyLong())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspeoPrenos("PravniRacun", prenosSredstava));
    }

    @Test
    public void shouldHandleFailedTransferForPravniRacunWithNullRacunPosiljaoca(){
        prenosSredstava = new PrenosSredstava(
                "1",                 // ID
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
    public void shouldHandleFailedTransferForDevizniRacun(){
        initObject();

        long vremeIzvrsavanja = System.currentTimeMillis();

        PrenosSredstava promenjenPrenosSredstava = new PrenosSredstava(
                "1",                 // ID
                123456L,            // RacunPosiljaoca
                789012L,            // RacunPrimaoca
                new BigDecimal("50.00"),  // Iznos
                1647162000L,        // Vreme (na primer: 15. mart 2022, 12:00)
                Status.NEUSPELO,           // Status
                vremeIzvrsavanja);

        Mockito.doReturn(promenjenPrenosSredstava).when(transakcijaServis).promeniStatusPrenosaSredstava(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());

        Mockito.doReturn(devizniRacun).when(racunServis).nadjiAktivanDevizniRacunPoBrojuRacuna(Mockito.anyLong());

        transakcijaServis.neuspeoPrenos("DevizniRacun", prenosSredstava);

        assertEquals(new BigDecimal("950.00"),devizniRacun.getRaspolozivoStanje());  // provera da li se vratio novac

    }

    @Test
    public void shouldHandleFailedTransferForNonExistentDevizniRacun(){
        initObject();
        Mockito.when(racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(Mockito.anyLong())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspeoPrenos("DevizniRacun", prenosSredstava));
    }

    @Test
    public void shouldHandleFailedTransferForDevizniRacunWithNullRacunPosiljaoca(){
        prenosSredstava = new PrenosSredstava(
                "1",                 // ID
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
                "1", // ID
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

        Mockito.doReturn(promenjenaUplata).when(transakcijaServis).promeniStatusUplate(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());

        transakcijaServis.neuspelaUplata("TekuciRacun", uplata);

        assertEquals(new BigDecimal("1000.00"),tekuciRacun.getRaspolozivoStanje());  // provera da li se vratio novac
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
                "1", // ID
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
    public void shouldHandleFailedPaymentForPravniRacun(){
        initObject();

        long vremeIzvrsavanja = System.currentTimeMillis() + 1000;

        Uplata promenjenaUplata = new Uplata(
                "1", // ID
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

        Mockito.doReturn(promenjenaUplata).when(transakcijaServis).promeniStatusUplate(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());

        Mockito.doReturn(pravniRacun).when(racunServis).nadjiAktivanPravniRacunPoBrojuRacuna(Mockito.anyLong());

        transakcijaServis.neuspelaUplata("PravniRacun", uplata);

        assertEquals(new BigDecimal("1000.00"),pravniRacun.getRaspolozivoStanje());  // provera da li se vratio novac
    }

    @Test
    public void shouldHandleFailedPaymentForNonExistentPravniRacun() {
        initObject();
        Mockito.when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(Mockito.anyLong())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspelaUplata("PravniRacun", uplata));
    }

    @Test
    public void shouldHandleFailedPaymentForPravniRacunWithNullRacunPosiljaoca(){
        uplata = new Uplata(
                "1", // ID
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
    public void shouldHandleFailedPaymentForDevizniRacun(){
        initObject();

        long vremeIzvrsavanja = System.currentTimeMillis() + 1000;

        Uplata promenjenaUplata = new Uplata(
                "1", // ID
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

        Mockito.doReturn(promenjenaUplata).when(transakcijaServis).promeniStatusUplate(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());

        Mockito.doReturn(devizniRacun).when(racunServis).nadjiAktivanDevizniRacunPoBrojuRacuna(Mockito.anyLong());

        transakcijaServis.neuspelaUplata("DevizniRacun", uplata);

        assertEquals(new BigDecimal("1000.00"),devizniRacun.getRaspolozivoStanje());  // provera da li se vratio novac
    }

    @Test
    public void shouldHandleFailedPaymentForNonExistentDevizniRacun(){
        initObject();
        Mockito.when(racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(Mockito.anyLong())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> transakcijaServis.neuspelaUplata("DevizniRacun", uplata));
    }

    @Test
    public void shouldHandleFailedPaymentForDevizniRacunWithNullRacunPosiljaoca(){
        uplata = new Uplata(
                "1", // ID
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
}
