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
    boolean reseed = true;

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
                if(reseed)
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
                if(reseed)
                    valuteRepository.saveAll(valute);


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
                if(reseed)
                    firmaRepository.saveAll(firme);

            List<DevizniRacun> dRacuni = new ArrayList<>();
            DevizniRacun dr1 = new DevizniRacun(444000000000000011L, 1L, new BigDecimal("10000")
                    , new BigDecimal("10000"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v2.getOznaka()
                    , true, new BigDecimal("1"), new BigDecimal(100 * 2));
            dRacuni.add(dr1);

            DevizniRacun dr2 = new DevizniRacun(444000000000000111L, 2L, new BigDecimal("10000")
                    , new BigDecimal("10000"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L,v2.getOznaka()
                    , true, new BigDecimal("1"), new BigDecimal(100 * 2));
            dRacuni.add(dr2);

            DevizniRacun dr3 = new DevizniRacun(444000000000000211L, 1L, new BigDecimal("10000")
                    , new BigDecimal("10000"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v3.getOznaka()
                    , true, new BigDecimal("1"), new BigDecimal(100 * 3));
            dRacuni.add(dr3);

            if(reseed){
                devizniRacunRepository.deleteAll();
                devizniRacunRepository.saveAll(dRacuni);
            }

            List<PravniRacun> pRacuni = new ArrayList<>();
            PravniRacun pr1 = new PravniRacun(444000000000000022L, -1L, new BigDecimal("10000")
                    , new BigDecimal("10000"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v8.getOznaka(), true);
            pRacuni.add(pr1);

            PravniRacun pr2 = new PravniRacun(444000000000000122L, -1L, new BigDecimal("10000")
                    , new BigDecimal("10000"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v2.getOznaka(), true);
            pRacuni.add(pr2);

            PravniRacun pr3 = new PravniRacun(444000000000000222L, -1L, new BigDecimal("10000")
                    , new BigDecimal("10000"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v3.getOznaka(), true);
            pRacuni.add(pr3);

            if(reseed){
                pravniRacunRepository.deleteAll();
                pravniRacunRepository.saveAll(pRacuni);
            }

            List<TekuciRacun> tRacuni = new ArrayList<>();
            TekuciRacun tr1 = new TekuciRacun(444000000000000033L, 1L, new BigDecimal("10000")
                    , new BigDecimal("10000"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v8.getOznaka()
                    , true, "Studentski", new BigDecimal("0.5"), new BigDecimal("0"));
            tRacuni.add(tr1);

            TekuciRacun tr2 = new TekuciRacun(444000000000000133L, 2L, new BigDecimal("11000")
                    , new BigDecimal("11000"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v8.getOznaka()
                    , true, "Studentski", new BigDecimal("0"), new BigDecimal("300"));
            tRacuni.add(tr2);

            TekuciRacun tr3 = new TekuciRacun(444000000000000233L, 1L, new BigDecimal("1000")
                    , new BigDecimal("1000"), 22222L, System.currentTimeMillis()
                    , System.currentTimeMillis() + 5*365*24*60*60*1000L, v8.getOznaka()
                    , true, "Studentski", new BigDecimal("0"), new BigDecimal("200"));
            tRacuni.add(tr3);

            if(reseed){
                tekuciRacunRepository.deleteAll();
                tekuciRacunRepository.saveAll(tRacuni);
            }
            MarzniRacun marzniRacun = new MarzniRacun(-1L, -1L, 444000000000000022L, "RSD", "STOCKS", new BigDecimal(10000000), new BigDecimal(10000000), new BigDecimal(500), new BigDecimal(50), false, null);
            if(marzniRacunRepository.findAll().isEmpty())
                if(reseed)
                    marzniRacunRepository.save(marzniRacun);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Scheduled(initialDelay = 60000)
    public void init(){
        System.out.println("dodavanje funkcija");
        try(Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
//            String sql = "CREATE OR REPLACE FUNCTION obrada_transakcije(brojRacunaUplatioca BIGINT, brojRacunaPrimaoca BIGINT, iznosUplate NUMERIC, iznosPrimaocu NUMERIC) RETURNS BOOLEAN AS $$\n" +
//                    "DECLARE\n" +00000
//                    "  aktivan_uplatilac BOOLEAN;\n" +
//                    "  aktivan_primalac BOOLEAN;\n" +
//                    "  rezervisanaSredstva NUMERIC;\n" +
//                    "  valuta_uplatilac VARCHAR;\n" +
//                    "  valuta_primalac VARCHAR;\n" +
//                    "  stanjeMenjacnicaPrimalac NUMERIC;\n" +
//                    "  postojiValutaUplatilac VARCHAR;\n" +
//                    "  postojiValutaPrimalac VARCHAR;\n" +
//                    "BEGIN\n" +
//                    "  SELECT aktivan, currency, stanje - raspolozivo_stanje \n" +
//                    "  INTO aktivan_uplatilac, valuta_uplatilac, rezervisanaSredstva\n" +
//                    "  FROM banka_schema.racun\n" +
//                    "  WHERE \"broj_racuna\" = brojRacunaUplatioca FOR UPDATE;\n" +
//                    "\n" +
//                    "  SELECT aktivan, currency\n" +
//                    "  INTO aktivan_primalac, valuta_primalac\n" +
//                    "  FROM banka_schema.racun\n" +
//                    "  WHERE \"broj_racuna\" = brojRacunaPrimaoca FOR UPDATE;\n" +
//                    "\n" +
//                    "  IF(aktivan_uplatilac IS FALSE OR aktivan_primalac IS FALSE) THEN\n" +
//                    "    RETURN FALSE;\n" +
//                    "  END IF;\n" +
//                    "  IF(rezervisanaSredstva < iznosUplate) THEN\n" +
//                    "    RETURN FALSE;\n" +
//                    "  END IF;\n" +
//                    "  IF(valuta_uplatilac = valuta_primalac) THEN\n" +
//                    "    UPDATE banka_schema.racun SET \"stanje\" = \"stanje\" - iznosUplate \n" +
//                    "    WHERE \"broj_racuna\" = brojRacunaUplatioca;\n" +
//                    "\n" +
//                    "    UPDATE banka_schema.racun SET \"stanje\" = \"stanje\" + iznosUplate,\n" +
//                    "    \"raspolozivo_stanje\" = \"raspolozivo_stanje\" + iznosUplate \n" +
//                    "    WHERE \"broj_racuna\" = brojRacunaPrimaoca;\n" +
//                    "    RETURN TRUE;\n" +
//                    "  END IF;\n" +
//                    "\n" +
//                    "  SELECT currency INTO postojiValutaUplatilac FROM banka_schema.exchange_account \n" +
//                    "  WHERE \"currency\" = valuta_uplatilac FOR UPDATE;\n" +
//                    "  IF(postojiValutaUplatilac IS NULL) THEN\n" +
//                    "    RETURN FALSE;\n" +
//                    "  END IF;\n" +
//                    "\n" +
//                    "  SELECT currency, stanje INTO postojiValutaPrimalac, stanjeMenjacnicaPrimalac FROM banka_schema.exchange_account\n" +
//                    "  WHERE \"currency\" = valuta_primalac FOR UPDATE;\n" +
//                    "  IF(postojiValutaPrimalac IS NULL OR stanjeMenjacnicaPrimalac < iznosPrimaocu) THEN\n" +
//                    "    RETURN FALSE;\n" +
//                    "  END IF;\n" +
//                    "\n" +
//                    "  UPDATE banka_schema.racun SET \"stanje\" = \"stanje\" - iznosUplate \n" +
//                    "  WHERE \"broj_racuna\" = brojRacunaUplatioca;\n" +
//                    "\n" +
//                    "  UPDATE banka_schema.racun SET \"stanje\" = \"stanje\" + iznosPrimaocu,\n" +
//                    "  \"raspolozivo_stanje\" = \"raspolozivo_stanje\" + iznosPrimaocu \n" +
//                    "  WHERE \"broj_racuna\" = brojRacunaPrimaoca;\n" +
//                    "\n" +
//                    "  UPDATE banka_schema.exchange_account SET \"stanje\" = \"stanje\" + iznosUplate \n" +
//                    "  WHERE \"currency\" = valuta_uplatilac;\n" +
//                    "\n" +
//                    "  UPDATE banka_schema.exchange_account SET \"stanje\" = \"stanje\" - iznosPrimaocu \n" +
//                    "  WHERE \"currency\" = valuta_primalac;\n" +
//                    "\n" +
//                    "  RETURN TRUE;\n" +
//                    "END $$ LANGUAGE plpgsql;";

            String sql = "CREATE OR REPLACE FUNCTION obrada_transakcije(brojRacunaUplatioca NUMERIC,brojRacunaPrimaoca NUMERIC,\n" +
                    "\t\t\t\t\t\tiznosUplate NUMERIC,iznosPrimaocu NUMERIC) RETURNS BOOLEAN AS $$\n" +
                    "DECLARE\n" +
                    "aktivan_uplatilac BOOLEAN;\n" +
                    "aktivan_primalac BOOLEAN;\n" +
                    "rezervisanaSredstva NUMERIC;\n" +
                    "valuta_uplatilac VARCHAR;\n" +
                    "valuta_primalac VARCHAR;\n" +
                    "stanjeMenjacnicaPrimalac NUMERIC;\n" +
                    "postojiValutaUplatilac VARCHAR;\n" +
                    "postojiValutaPrimalac VARCHAR;\n" +
                    "BEGIN\n" +
                    "\tSELECT aktivan,currency,stanje - raspolozivo_stanje \n" +
                    "\tINTO aktivan_uplatilac,valuta_uplatilac,rezervisanaSredstva\n" +
                    "\tFROM banka_schema.racun\n" +
                    "\tWHERE \"broj_racuna\" = brojRacunaUplatioca FOR UPDATE;\n" +
                    "\t\n" +
                    "\tSELECT aktivan,currency\n" +
                    "\tINTO aktivan_primalac,valuta_primalac\n" +
                    "\tFROM banka_schema.racun\n" +
                    "\tWHERE \"broj_racuna\" = brojRacunaPrimaoca FOR UPDATE;\n" +
                    "\t\n" +
                    "\tIF(aktivan_uplatilac IS FALSE OR aktivan_primalac IS FALSE) THEN\n" +
                    "-- \t\tRETURN FALSE;\n" +
                    "\t\tRAISE EXCEPTION 'Neki nalog nije aktivan';\n" +
                    "\tEND IF;\n" +
                    "\tIF(rezervisanaSredstva < iznosUplate) THEN\n" +
                    "-- \t\tRETURN FALSE;\n" +
                    "\t\tRAISE EXCEPTION 'Nema dovoljno rezervisanih sredstava';\n" +
                    "\tEND IF;\n" +
                    "\tIF(valuta_uplatilac = valuta_primalac) THEN\n" +
                    "\t\tUPDATE banka_schema.racun SET \"stanje\" = \"stanje\" - iznosUplate \n" +
                    "\t\tWHERE \"broj_racuna\" = brojRacunaUplatioca;\n" +
                    "\n" +
                    "\t\tUPDATE banka_schema.racun SET \"stanje\" = \"stanje\" + iznosUplate,\n" +
                    "\t\t\"raspolozivo_stanje\" = \"raspolozivo_stanje\" + iznosUplate \n" +
                    "\t\tWHERE \"broj_racuna\" = brojRacunaPrimaoca;\n" +
                    "\t\tRETURN TRUE;\n" +
                    "\tEND IF;\n" +
                    "\t\n" +
                    "\tSELECT currency INTO postojiValutaUplatilac FROM banka_schema.exchange_account \n" +
                    "\tWHERE \"currency\" = valuta_uplatilac FOR UPDATE;\n" +
                    "\tIF(postojiValutaUplatilac IS NULL) THEN\n" +
                    "\t\tRAISE EXCEPTION 'Ne postoji valuta kojom se uplacuje';\n" +
                    "\tEND IF;\n" +
                    "\t\n" +
                    "\tSELECT currency,stanje INTO postojiValutaPrimalac,stanjeMenjacnicaPrimalac FROM banka_schema.exchange_account\n" +
                    "\tWHERE \"currency\" = valuta_primalac FOR UPDATE;\n" +
                    "\tIF(postojiValutaPrimalac IS NULL OR stanjeMenjacnicaPrimalac < iznosPrimaocu) THEN\n" +
                    "\t\tRAISE EXCEPTION 'Ne postoji valuta za isplatu ili menjacnica nema dovoljno u valuti za isplatu';\n" +
                    "\tEND IF;\n" +
                    "\t\n" +
                    "\tUPDATE banka_schema.racun SET \"stanje\" = \"stanje\" - iznosUplate \n" +
                    "\tWHERE \"broj_racuna\" = brojRacunaUplatioca;\n" +
                    "\n" +
                    "\tUPDATE banka_schema.racun SET \"stanje\" = \"stanje\" + iznosPrimaocu,\n" +
                    "\t\"raspolozivo_stanje\" = \"raspolozivo_stanje\" + iznosPrimaocu \n" +
                    "\tWHERE \"broj_racuna\" = brojRacunaPrimaoca;\n" +
                    "\t\n" +
                    "\tUPDATE banka_schema.exchange_account SET \"stanje\" = \"stanje\" + iznosUplate \n" +
                    "\tWHERE \"currency\" = valuta_uplatilac;\n" +
                    "\n" +
                    "\tUPDATE banka_schema.exchange_account SET \"stanje\" = \"stanje\" - iznosPrimaocu \n" +
                    "\tWHERE \"currency\" = valuta_primalac;\n" +
                    "\t\n" +
                    "\tRETURN TRUE;\n" +
                    "END $$ LANGUAGE plpgsql;" +
                    "CREATE OR REPLACE FUNCTION prihvatiPonudu(ponudaID NUMERIC) RETURNS VOID AS $$\n" +
                    "DECLARE\n" +
                    "brojRacuna NUMERIC;\n" +
                    "\tstockId NUMERIC;\n" +
                    "\tstockQuantity NUMERIC;\n" +
                    "\tamountOffered NUMERIC;\n" +
                    "BEGIN\n" +
                    "SELECT quantity,amount_offered, user_stock_id INTO stockQuantity,amountOffered,stockId\n" +
                    "FROM berza_schema.ponuda\n" +
                    "WHERE \"id\" = ponudaID;\n" +
                    "\n" +
                    "SELECT broj_racuna INTO brojRacuna FROM banka_schema.marzni_racun WHERE \"vlasnik\" = -1;\n" +
                    "\n" +
                    "UPDATE banka_schema.racun\n" +
                    "SET \"stanje\" = \"stanje\" + amountOffered\n" +
                    "WHERE \"broj_racuna\" = brojRacuna;\n" +
                    "\n" +
                    "UPDATE berza_schema.user_stocks\n" +
                    "SET \"quantity\" = \"quantity\" + stockQuantity\n" +
                    "WHERE \"id\" = stockId;\n" +
                    "END $$ LANGUAGE plpgsql;\n" +
                    "\n" +
                    "CREATE OR REPLACE FUNCTION confirmFuture() RETURNS VOID AS $$\n" +
                    "BEGIN\n" +
                    "UPDATE banka_schema.racun r\n" +
                    "SET stanje = stanje - f.price\n" +
                    "    FROM berza_schema.futures_contract f\n" +
                    "WHERE r.broj_racuna = f.racun_id\n" +
                    "  AND to_timestamp(f.settlement_date / 1000) < CURRENT_DATE;\n" +
                    "\n" +
                    "UPDATE berza_schema.futures_contract\n" +
                    "SET bought = TRUE\n" +
                    "WHERE to_timestamp(settlement_date / 1000) < CURRENT_DATE\n" +
                    "  AND racun_id IS NOT NULL;\n" +
                    "END $$ LANGUAGE plpgsql;\n" +
                    "\n" +
                    "\n" +
                    "CREATE OR REPLACE FUNCTION prebaci(amount NUMERIC, brojRacuna BIGINT, idMarznog BIGINT) RETURNS BOOLEAN AS $$\n" +
                    "DECLARE\n" +
                    "stanjeRacuna NUMERIC;\n" +
                    "BEGIN\n" +
                    "SELECT stanje FROM banka_schema.racun INTO stanjeRacuna WHERE \"broj_racuna\" = brojRacuna FOR UPDATE;\n" +
                    "\n" +
                    "IF(stanjeRacuna >= amount) THEN\n" +
                    "UPDATE banka_schema.racun\n" +
                    "SET \"stanje\" = \"stanje\" - amount\n" +
                    "WHERE \"broj_racuna\" = brojRacuna;\n" +
                    "\n" +
                    "UPDATE banka_schema.marzni_racun\n" +
                    "SET \"liquid_cash\" = \"liquid_cash\" + amount, \"margin_call\" = FALSE\n" +
                    "WHERE \"id\" = idMarznog;\n" +
                    "RETURN TRUE;\n" +
                    "END IF;\n" +
                    "RETURN FALSE;\n" +
                    "END $$ LANGUAGE plpgsql;" +
                    "CREATE OR REPLACE FUNCTION bankaaa(stockTicker VARCHAR, stockQuantity BIGINT, amountOffered NUMERIC) RETURNS VOID AS $$\n" +
                    "DECLARE\n" +
                    "    brojRacuna NUMERIC;\n" +
                    "\tstockId NUMERIC;\n" +
                    "\tstockExists NUMERIC;\n" +
                    "BEGIN\n" +
                    "SELECT broj_racuna INTO brojRacuna FROM banka_schema.marzni_racun WHERE \"vlasnik\" = -1;\n" +
                    "\n" +
                    "UPDATE banka_schema.racun\n" +
                    "SET \"stanje\" = \"stanje\" - amountOffered\n" +
                    "WHERE \"broj_racuna\" = brojRacuna;\n" +
                    "\n" +
                    "SELECT COUNT(*) INTO stockExists FROM berza_schema.user_stocks WHERE \"ticker\" = stockTicker FOR UPDATE;\n" +
                    "\n" +
                    "IF (stockExists > 0) THEN\n" +
                    "UPDATE berza_schema.user_stocks\n" +
                    "SET \"quantity\" = \"quantity\" + stockQuantity\n" +
                    "WHERE \"ticker\" = stockTicker;\n" +
                    "ELSE\n" +
                    "\t\tINSERT INTO berza_schema.user_stocks (user_id,ticker,quantity,current_bid,current_ask)\n" +
                    "\t\tVALUES(-1,stockTicker,stockQuantity,0,0);\n" +
                    "END IF;\n" +
                    "END $$ LANGUAGE plpgsql;\n" +
                    "\n" +
                    "CREATE OR REPLACE FUNCTION kupi_future_contract(radnik_id BIGINT, future_contract_id BIGINT, broj_racuna_id BIGINT) RETURNS VOID AS $$\n" +
                    "DECLARE\n" +
                    "    dailySpentPlusPrice NUMERIC;\n" +
                    "    totalLimit NUMERIC;\n" +
                    "    contractPrice NUMERIC;\n" +
                    "balance NUMERIC;\n" +
                    "isSupervisor BOOLEAN;\n" +
                    "isApprovalFlag BOOLEAN;\n" +
                    "kupac NUMERIC;\n" +
                    "firmaId NUMERIC;\n" +
                    "settlementDate NUMERIC;\n" +
                    "\n" +
                    "radnikEmail VARCHAR;\n" +
                    "radnikIme VARCHAR;\n" +
                    "radnikPrezime VARCHAR;\n" +
                    "brojTelefona VARCHAR;\n" +
                    "\n" +
                    "BEGIN\n" +
                    "    SELECT daily_spent,daily_limit,supervisor,approval_flag,firma_id INTO dailySpentPlusPrice,totalLimit,isSupervisor,isApprovalFlag,firmaId FROM user_schema.radnik WHERE \"id\" = radnik_id FOR UPDATE;\n" +
                    "    SELECT price,kupac_id,settlement_date INTO contractPrice,kupac,settlementDate FROM berza_schema.futures_contract WHERE \"id\" = future_contract_id FOR UPDATE;\n" +
                    "SELECT raspolozivo_stanje INTO balance FROM banka_schema.racun WHERE \"broj_racuna\" = broj_racuna_id FOR UPDATE;\n" +
                    "\n" +
                    "IF(kupac IS NOT NULL) THEN\n" +
                    "RAISE EXCEPTION 'Future contract je vec kupljen';\n" +
                    "END IF;\n" +
//                    "IF(CURRENT_DATE > to_timestamp(settlementDate / 1000)) THEN\n" +
//                    "RAISE EXCEPTION 'Future contract je istekao';\n" +
//                    "END IF;\n" +
                    "    -- Izračunavanje ukupnog dnevnog troška plus cene future contracta\n" +
                    "    dailySpentPlusPrice := dailySpentPlusPrice + contractPrice;\n" +
                    "\n" +
                    "    -- Provera da li je ukupni dnevni trošak manji od dnevnog limita\n" +
                    "    IF (contractPrice <= balance) THEN\n" +
                    "        -- Provera da li ima dovoljno raspoloživog novca na tekucem racunu za kupovinu future contracta\n" +
                    "        IF((isSupervisor IS TRUE OR (dailySpentPlusPrice <= totalLimit AND isApprovalFlag IS FALSE))) THEN\n" +
                    "UPDATE banka_schema.racun\n" +
                    "SET \"raspolozivo_stanje\" = \"raspolozivo_stanje\" - contractPrice\n" +
                    "WHERE \"broj_racuna\" = broj_racuna_id;\n" +
                    "\n" +
                    "-- Ažuriranje dailySpent radnika\n" +
                    "UPDATE user_schema.radnik SET \"daily_spent\" = \"daily_spent\" + contractPrice WHERE \"id\" = radnik_id;\n" +
                    "\n" +
                    "-- Ažuriranje kupca u future contractu\n" +
                    "UPDATE berza_schema.futures_contract SET \"kupac_id\" = firmaId,\"racun_id\"=broj_racuna_id WHERE \"id\" = future_contract_id;\n" +
                    "        ELSE\n" +
                    "SELECT email,ime,prezime,broj_telefona INTO radnikEmail,radnikIme,radnikPrezime,brojTelefona FROM user_schema.radnik WHERE \"id\" = radnik_id;\n" +
                    "INSERT INTO berza_schema.future_contract_request (firma_id,futures_contract_id,racun_id,radnik_id,broj_telefona,email,ime,prezime,request_status)\n" +
                    "VALUES (firmaId,future_contract_id,broj_racuna_id,radnik_id,brojTelefona,radnikEmail,radnikIme,radnikPrezime,'NOT_APPROVED');\n" +
                    "   RAISE NOTICE 'Potrebno odobrenje za transakciju';\n" +
                    "        -- Potvrda transakcije\n" +
                    "END IF;\n" +
                    "    ELSE\n" +
                    "RAISE EXCEPTION 'Nema dovoljno novca na racunu.';\n" +
                    "END IF;\n" +
                    "END $$ LANGUAGE plpgsql;\n" +
                    "\n" +
                    "\n" +
                    "CREATE OR REPLACE FUNCTION approve(contract_id NUMERIC, supervisor_id NUMERIC) RETURNS VOID AS $$\n" +
                    "DECLARE\n" +
                    "balance NUMERIC; -- iskorisceno\n" +
                    "kupac NUMERIC; -- iskorisceno\n" +
                    "firmaId NUMERIC;\n" +
                    "settlementDate NUMERIC; --iskorisceno\n" +
                    "contractPrice NUMERIC; -- iskorisceno\n" +
                    "\n" +
                    "racunId NUMERIC; -- iskorisceno\n" +
                    "radnikId NUMERIC; -- iskorisceno\n" +
                    "radnik_firma_id NUMERIC; -- iskorisceno\n" +
                    "requestStatus VARCHAR; -- iskorisceno\n" +
                    "futureContractId NUMERIC;\n" +
                    "\n" +
                    "BEGIN\n" +
                    "SELECT request_status,firma_id,settlement_date,kupac_id,price,racun_id,radnik_id,futures_contract_id\n" +
                    "INTO requestStatus,radnik_firma_id,settlementDate,kupac,contractPrice,racunId,radnikId,futureContractId\n" +
                    "FROM berza_schema.future_contract_request fcr JOIN berza_schema.futures_contract fc\n" +
                    "ON (fcr.futures_contract_id = fc.id)\n" +
                    "WHERE fcr.futures_contract_id = contract_id\n" +
                    "FOR UPDATE;\n" +
                    "\n" +
                    "SELECT raspolozivo_stanje INTO balance FROM banka_schema.racun\n" +
                    "WHERE \"broj_racuna\" = racunId FOR UPDATE;\n" +
                    "\n" +
                    "SELECT firma_id INTO firmaId FROM user_schema.radnik WHERE id = supervisor_id FOR UPDATE;\n" +
                    "\n" +
                    "IF(kupac IS NOT NULL) THEN\n" +
                    "RAISE EXCEPTION 'Future contract je vec kupljen';\n" +
                    "END IF;\n" +
                    "\n" +
//                    "IF(CURRENT_DATE > to_timestamp(settlementDate / 1000)) THEN\n" +
//                    "RAISE EXCEPTION 'Future contract je istekao';\n" +
//                    "END IF;\n" +
                    "\n" +
                    "IF(firmaId <> radnik_firma_id) THEN\n" +
                    "RAISE EXCEPTION 'Ovaj zahtev nije za vašu firmu!';\n" +
                    "END IF;\n" +
                    "\n" +
                    "IF(requestStatus <> 'NOT_APPROVED') THEN\n" +
                    "RAISE EXCEPTION 'Vec je obradjen zahtev';\n" +
                    "END IF;\n" +
                    "    -- Provera da li je ukupni dnevni trošak manji od dnevnog limita\n" +
                    "    IF (contractPrice <= balance) THEN\n" +
                    "        -- Provera da li ima dovoljno raspoloživog novca na tekucem racunu za kupovinu future contracta\n" +
                    "UPDATE banka_schema.racun\n" +
                    "SET \"raspolozivo_stanje\" = \"raspolozivo_stanje\" - contractPrice\n" +
                    "WHERE \"broj_racuna\" = racunId;\n" +
                    "\n" +
                    "-- Ažuriranje dailySpent radnika\n" +
                    "UPDATE user_schema.radnik SET \"daily_spent\" = \"daily_spent\" + contractPrice WHERE \"id\" = radnikId;\n" +
                    "\n" +
                    "-- Ažuriranje kupca u future contractu\n" +
                    "UPDATE berza_schema.futures_contract SET \"kupac_id\" = firmaId,\"racun_id\"=racunId WHERE \"id\" = futureContractId;\n" +
                    "    UPDATE berza_schema.future_contract_request SET \"request_status\" = 'APPROVED' WHERE \"id\" = contract_id;\n" +
                    "ELSE\n" +
                    "RAISE EXCEPTION 'Nema dovoljno novca na racunu.';\n" +
                    "END IF;\n" +
                    "END $$ LANGUAGE plpgsql;";
            statement.execute(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
