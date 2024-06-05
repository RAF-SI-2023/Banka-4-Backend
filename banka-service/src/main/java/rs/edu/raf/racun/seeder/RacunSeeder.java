package rs.edu.raf.racun.seeder;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.model.entities.racun.*;
import rs.edu.raf.repository.racun.FirmaRepository;
import rs.edu.raf.repository.racun.ValuteRepository;
import rs.edu.raf.repository.racun.ZemljaRepository;
import rs.edu.raf.repository.transaction.*;
import rs.edu.raf.service.transaction.TransakcijaServis;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



@Component
public class RacunSeeder implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    private final ZemljaRepository zemljaRepository;
    private final ValuteRepository valuteRepository;
    private final FirmaRepository firmaRepository;

    private final DevizniRacunRepository devizniRacunRepository;
    private final PravniRacunRepository pravniRacunRepository;


    private final MarzniRacunRepository marzniRacunRepository;

    private final TekuciRacunRepository tekuciRacunRepository;
    //private final KorisnikRepository korisnikRepository;
    //private final RadnikRepository radnikRepository;
    @Autowired
    private UplataRepository uplataRepository;
    @Autowired
    private PrenosSredstavaRepository prenosSredstavaRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TransakcijaServis transakcijaServis;

    @Autowired
    public RacunSeeder(ZemljaRepository zemljaRepository, ValuteRepository valuteRepository, FirmaRepository firmaRepository, DevizniRacunRepository devizniRacunRepository, PravniRacunRepository pravniRacunRepository, TekuciRacunRepository tekuciRacunRepository, UplataRepository uplataRepository, PrenosSredstavaRepository prenosSredstavaRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TransakcijaServis transakcijaServis, MarzniRacunRepository marzniRacunRepository) {
        this.zemljaRepository = zemljaRepository;
        this.valuteRepository = valuteRepository;
        this.firmaRepository = firmaRepository;
        this.devizniRacunRepository = devizniRacunRepository;
        this.pravniRacunRepository = pravniRacunRepository;
        this.tekuciRacunRepository = tekuciRacunRepository;
        //this.korisnikRepository = korisnikRepository;
        //this.radnikRepository = radnikRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.transakcijaServis = transakcijaServis;
        this.marzniRacunRepository = marzniRacunRepository;
    }

    //automatski se izvrsava pri pokretanju spring boota za dodavanje entiteta u bazu
    @Override
    public void run(String... args) throws Exception {

        try {
/*
            uplataRepository.deleteAll();
            uplataRepository.findAll().forEach(System.out::println);
            prenosSredstavaRepository.deleteAll();
            prenosSredstavaRepository.findAll().forEach(System.out::println);
*/
            List<Zemlja> zemlje = new ArrayList<>();
            Zemlja z1 = new Zemlja("Švajcarska Konfederacija");
            zemlje.add(z1);
            Zemlja z2 = new Zemlja("Sjedinjene Američke Države");
            zemlje.add(z2);
            Zemlja z3 = new Zemlja("Francuska Republika");
            zemlje.add(z3);
            Zemlja z4 = new Zemlja("Savezna Republika Nemačka");
            zemlje.add(z4);
            Zemlja z5 = new Zemlja("Ujedinjeno Kraljevstvo Velike Britanije i Severne Irske");
            zemlje.add(z5);
            Zemlja z6 = new Zemlja("Japan");
            zemlje.add(z6);
            Zemlja z7 = new Zemlja("Kanada");
            zemlje.add(z7);
            Zemlja z8 = new Zemlja("Komonvelt Australija");
            zemlje.add(z8);
            Zemlja z9 = new Zemlja("Republika Srbija");
            zemlje.add(z9);
            if(zemljaRepository.findAll().isEmpty())
                this.zemljaRepository.saveAll(zemlje);

            List<Valute> valute = new ArrayList<>();
            Valute v1 = new Valute("Švajcarski franak", "CHF", "fr.", z1.getNaziv());
            valute.add(v1);
            Valute v2 = new Valute("Američki dolar", "USD", "$", z2.getNaziv());
            valute.add(v2);
            Valute v3 = new Valute("Evro", "EUR", "€", z3.getNaziv() + "," + z4.getNaziv());
            valute.add(v3);
            Valute v4 = new Valute("Britanska funta", "GBP", "£", z5.getNaziv());
            valute.add(v4);
            Valute v5 = new Valute("Japanski jen", "JPY", "¥", z6.getNaziv());
            valute.add(v5);
            Valute v6 = new Valute("Kanadijski dolar", "CAD", "$", z7.getNaziv());
            valute.add(v6);
            Valute v7 = new Valute("Australijski dolar", "AUD", "$", z8.getNaziv());
            valute.add(v7);
            Valute v8 = new Valute("Srpski dinar", "RSD", "дин.", z9.getNaziv());
            valute.add(v8);
            if(valuteRepository.findAll().isEmpty())
                valuteRepository.saveAll(valute);

            MarzniRacun marzniRacun = new MarzniRacun(-1L, -1L, 444000000000000022L, "RSD", "STOCKS", new BigDecimal(10000000), new BigDecimal(10000000), new BigDecimal(500), new BigDecimal(50), false, null);
            if(marzniRacunRepository.findAll().isEmpty())
                marzniRacunRepository.save(marzniRacun);

            List<Firma> firme = new ArrayList<>();
            Firma f1 = new Firma(-1L, "Nasa banka", "444000000000000022,444000000000000122,444000000000000222"
                    , "0112030403", "0112030402", 101017533, 17328905
                    , 6102, 130501701);
            firme.add(f1);
            Firma f2 = new Firma(-2L, "Factory World Wide", ""
                    , "0112030403", "0112030402", 101017533, 17328905
                    , 6102, 130501701);
            firme.add(f2);
            Firma f3 = new Firma(-3L, "EM Analytic Solutions", ""
                    , "0112030403", "0112030402", 101017533, 17328905
                    , 6102, 130501701);
            firme.add(f3);
            if(firmaRepository.findAll().isEmpty())
                firmaRepository.saveAll(firme);

            List<DevizniRacun> dRacuni = new ArrayList<>();
            DevizniRacun dr1 = new DevizniRacun(444000000000000011L, 1L, new BigDecimal("10000")
                    , new BigDecimal("0"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v2.getOznaka()
                    , true, new BigDecimal("1"), new BigDecimal(100 * 2));
            if(!(devizniRacunRepository.findByBrojRacunaAndAktivanIsTrue(dr1.getBrojRacuna()).isPresent()))
                dRacuni.add(dr1);

            DevizniRacun dr2 = new DevizniRacun(444000000000000111L, 2L, new BigDecimal("10000")
                    , new BigDecimal("0"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L,v2.getOznaka()
                    , true, new BigDecimal("1"), new BigDecimal(100 * 2));
            if(!(devizniRacunRepository.findByBrojRacunaAndAktivanIsTrue(dr2.getBrojRacuna()).isPresent()))
                dRacuni.add(dr2);

            DevizniRacun dr3 = new DevizniRacun(444000000000000211L, 1L, new BigDecimal("10000")
                    , new BigDecimal("0"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v3.getOznaka()
                    , true, new BigDecimal("1"), new BigDecimal(100 * 3));
            if(!(devizniRacunRepository.findByBrojRacunaAndAktivanIsTrue(dr3.getBrojRacuna()).isPresent()))
                dRacuni.add(dr3);

            devizniRacunRepository.saveAll(dRacuni);

            List<PravniRacun> pRacuni = new ArrayList<>();
            PravniRacun pr1 = new PravniRacun(444000000000000022L, -1L, new BigDecimal("10000")
                    , new BigDecimal("10000"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v8.getOznaka(), true);
            if(!(pravniRacunRepository.findByBrojRacunaAndAktivanIsTrue(pr1.getBrojRacuna()).isPresent()))
                pRacuni.add(pr1);

            PravniRacun pr2 = new PravniRacun(444000000000000122L, -1L, new BigDecimal("10000")
                    , new BigDecimal("10000"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v2.getOznaka(), true);
            if(!(pravniRacunRepository.findByBrojRacunaAndAktivanIsTrue(pr2.getBrojRacuna()).isPresent()))
                pRacuni.add(pr2);

            PravniRacun pr3 = new PravniRacun(444000000000000222L, -1L, new BigDecimal("10000")
                    , new BigDecimal("10000"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v3.getOznaka(), true);
            if(!(pravniRacunRepository.findByBrojRacunaAndAktivanIsTrue(pr3.getBrojRacuna()).isPresent()))
                pRacuni.add(pr3);

            pravniRacunRepository.saveAll(pRacuni);

            List<TekuciRacun> tRacuni = new ArrayList<>();
            TekuciRacun tr1 = new TekuciRacun(444000000900000033L, 1L, new BigDecimal("10000")
                    , new BigDecimal("10000"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v8.getOznaka()
                    , true, "Studentski", new BigDecimal("0.5"), new BigDecimal("0"));
            if(!(tekuciRacunRepository.findByBrojRacunaAndAktivanIsTrue(tr1.getBrojRacuna()).isPresent()))
                tRacuni.add(tr1);

            TekuciRacun tr2 = new TekuciRacun(444000000910000033L, 2L, new BigDecimal("11000")
                    , new BigDecimal("11000"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v8.getOznaka()
                    , true, "Studentski", new BigDecimal("0"), new BigDecimal("300"));
            if(!(tekuciRacunRepository.findByBrojRacunaAndAktivanIsTrue(tr2.getBrojRacuna()).isPresent()))
                tRacuni.add(tr2);

            TekuciRacun tr3 = new TekuciRacun(444000000920000033L, 1L, new BigDecimal("1000")
                    , new BigDecimal("1000"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v8.getOznaka()
                    , true, "Studentski", new BigDecimal("0"), new BigDecimal("200"));
            if(!(tekuciRacunRepository.findByBrojRacunaAndAktivanIsTrue(tr3.getBrojRacuna()).isPresent()))
                tRacuni.add(tr3);

            tekuciRacunRepository.saveAll(tRacuni);



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Scheduled(initialDelay = 60000)
    public void init(){
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            String sql = "CREATE OR REPLACE FUNCTION obrada_transakcije(brojRacunaUplatioca BIGINT, brojRacunaPrimaoca BIGINT, iznosUplate NUMERIC, iznosPrimaocu NUMERIC) RETURNS BOOLEAN AS $$\n" +
                    "DECLARE\n" +
                    "  aktivan_uplatilac BOOLEAN;\n" +
                    "  aktivan_primalac BOOLEAN;\n" +
                    "  rezervisanaSredstva NUMERIC;\n" +
                    "  valuta_uplatilac VARCHAR;\n" +
                    "  valuta_primalac VARCHAR;\n" +
                    "  stanjeMenjacnicaPrimalac NUMERIC;\n" +
                    "  postojiValutaUplatilac VARCHAR;\n" +
                    "  postojiValutaPrimalac VARCHAR;\n" +
                    "BEGIN\n" +
                    "  SELECT aktivan, currency, stanje - raspolozivo_stanje \n" +
                    "  INTO aktivan_uplatilac, valuta_uplatilac, rezervisanaSredstva\n" +
                    "  FROM banka_schema.racun\n" +
                    "  WHERE \"broj_racuna\" = brojRacunaUplatioca FOR UPDATE;\n" +
                    "\n" +
                    "  SELECT aktivan, currency\n" +
                    "  INTO aktivan_primalac, valuta_primalac\n" +
                    "  FROM banka_schema.racun\n" +
                    "  WHERE \"broj_racuna\" = brojRacunaPrimaoca FOR UPDATE;\n" +
                    "\n" +
                    "  IF(aktivan_uplatilac IS FALSE OR aktivan_primalac IS FALSE) THEN\n" +
                    "    RETURN FALSE;\n" +
                    "  END IF;\n" +
                    "  IF(rezervisanaSredstva < iznosUplate) THEN\n" +
                    "    RETURN FALSE;\n" +
                    "  END IF;\n" +
                    "  IF(valuta_uplatilac = valuta_primalac) THEN\n" +
                    "    UPDATE banka_schema.racun SET \"stanje\" = \"stanje\" - iznosUplate \n" +
                    "    WHERE \"broj_racuna\" = brojRacunaUplatioca;\n" +
                    "\n" +
                    "    UPDATE banka_schema.racun SET \"stanje\" = \"stanje\" + iznosUplate,\n" +
                    "    \"raspolozivo_stanje\" = \"raspolozivo_stanje\" + iznosUplate \n" +
                    "    WHERE \"broj_racuna\" = brojRacunaPrimaoca;\n" +
                    "    RETURN TRUE;\n" +
                    "  END IF;\n" +
                    "\n" +
                    "  SELECT currency INTO postojiValutaUplatilac FROM banka_schema.exchange_account \n" +
                    "  WHERE \"currency\" = valuta_uplatilac FOR UPDATE;\n" +
                    "  IF(postojiValutaUplatilac IS NULL) THEN\n" +
                    "    RETURN FALSE;\n" +
                    "  END IF;\n" +
                    "\n" +
                    "  SELECT currency, stanje INTO postojiValutaPrimalac, stanjeMenjacnicaPrimalac FROM banka_schema.exchange_account\n" +
                    "  WHERE \"currency\" = valuta_primalac FOR UPDATE;\n" +
                    "  IF(postojiValutaPrimalac IS NULL OR stanjeMenjacnicaPrimalac < iznosPrimaocu) THEN\n" +
                    "    RETURN FALSE;\n" +
                    "  END IF;\n" +
                    "\n" +
                    "  UPDATE banka_schema.racun SET \"stanje\" = \"stanje\" - iznosUplate \n" +
                    "  WHERE \"broj_racuna\" = brojRacunaUplatioca;\n" +
                    "\n" +
                    "  UPDATE banka_schema.racun SET \"stanje\" = \"stanje\" + iznosPrimaocu,\n" +
                    "  \"raspolozivo_stanje\" = \"raspolozivo_stanje\" + iznosPrimaocu \n" +
                    "  WHERE \"broj_racuna\" = brojRacunaPrimaoca;\n" +
                    "\n" +
                    "  UPDATE banka_schema.exchange_account SET \"stanje\" = \"stanje\" + iznosUplate \n" +
                    "  WHERE \"currency\" = valuta_uplatilac;\n" +
                    "\n" +
                    "  UPDATE banka_schema.exchange_account SET \"stanje\" = \"stanje\" - iznosPrimaocu \n" +
                    "  WHERE \"currency\" = valuta_primalac;\n" +
                    "\n" +
                    "  RETURN TRUE;\n" +
                    "END $$ LANGUAGE plpgsql;";

            statement.execute(sql);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
