package rs.edu.raf.opcija.servis.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.opcija.dto.NovaOpcijaDto;
import rs.edu.raf.opcija.dto.OpcijaDto;
import rs.edu.raf.opcija.dto.OpcijaKorisnikaDto;
import rs.edu.raf.opcija.dto.OptionChainResponse;
import rs.edu.raf.opcija.mapper.OpcijaMapper;
import rs.edu.raf.opcija.model.*;
import rs.edu.raf.opcija.repository.GlobalQuoteRepository;
import rs.edu.raf.opcija.repository.KorisnikRepository;
import rs.edu.raf.opcija.servis.IzvedeneVrednostiUtil;
import rs.edu.raf.opcija.servis.OpcijaServis;
import rs.edu.raf.opcija.servis.util.FinansijaApiUtil;
import rs.edu.raf.opcija.servis.util.GlobalQuoteApiMap;
import rs.edu.raf.opcija.servis.util.OptionYahooApiMap;
import rs.edu.raf.opcija.repository.KorisnikoveKupljeneOpcijeRepository;
import rs.edu.raf.opcija.repository.OpcijaRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

//direktno komunicira sa bazom i sluzi za operacije nad entitetom pre upisa u bazu
@Service
public class OpcijaServisImpl implements OpcijaServis {

    private final static Logger log = LoggerFactory.getLogger(OpcijaServisImpl.class.getSimpleName());

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private IzvedeneVrednostiUtil izvedeneVrednostiUtil;

    @Autowired
    private FinansijaApiUtil finansijaApiUtil;

    @Autowired
    private KorisnikoveKupljeneOpcijeRepository korisnikKupljeneOpcijeRepository;

    @Autowired
    private GlobalQuoteRepository akcijaRepository;

    @Autowired
    private KorisnikRepository korisnikRepository;

    @Autowired
    private OpcijaMapper opcijaMapper;

    @Autowired
    private OpcijaRepository opcijaRepository;


    private List<Opcija> fetchAllOptionsForAllTickers() throws IOException {

        List<String> tickerNames = finansijaApiUtil.fetchTickerNames();

        if(tickerNames.size() == 0)
            return new ArrayList<>();
                                                                                                                //staviti na sve tickerNames
                                                                                                                //Collections.singletonList(tickerNames.get(0))
        List<OptionYahooApiMap> yahooOpcije = finansijaApiUtil.fetchOptionsFromYahooApi(tickerNames.subList(0, 4));

        //log.info(String.valueOf(System.currentTimeMillis()));
        log.info("Gotovo fetchovanje sa yahoo api");
        return yahooOpcije.stream().map(yahooOpcija -> opcijaMapper.yahooOpcijaToOpcija(yahooOpcija)).collect(Collectors.toList());
    }
    private List<GlobalQuote> fetchAllGlobalQuote() throws IOException {

        List<String> tickerNames = finansijaApiUtil.fetchTickerNames();

        if(tickerNames.size() == 0)
            return new ArrayList<>();
                                                                                            //staviti na sve tickerNames
                                                                                            //Collections.singletonList(tickerNames.get(0))
        List<GlobalQuoteApiMap> globalQuotes = finansijaApiUtil.fetchGlobalQuote(tickerNames.subList(0, 4));

        if(globalQuotes.size() == 0) {
            log.info("Nije uspelo fetchovanje sa alphavantage api");
            return new ArrayList<>();
        }
        log.info("Gotovo fetchovanje sa alphavantage api");

        return globalQuotes.stream().map(globalQuote -> opcijaMapper.globalQuoteApiToGlobalQuote(globalQuote)).collect(Collectors.toList());
    }

    //CITA IZ CACHE A AKO NE POSTOJI ONDA IZ BAZE
    @Override
    public List<OpcijaDto> findAll()  {

        List<Opcija> opcije = new ArrayList<>();
        Cache cache = cacheManager.getCache("opcijeCache");

        if (cache != null) {
            Map<Object, Object> cacheMap = (Map<Object, Object>) cache.getNativeCache();
            if (cacheMap != null) {
                for (Map.Entry<Object, Object> entry : cacheMap.entrySet()) {
                    Object value = entry.getValue();
                    if (value instanceof Opcija) {
                        opcije.add((Opcija) value);
                    }
                }
            }
        }
        if(opcije.size() != 0) {
            log.info("findAll iz cache");
            return opcije.stream().map(opcija -> opcijaMapper.opcijaToOpcijaDto(opcija)).collect(Collectors.toList());
        }
            opcije = opcijaRepository.findAll();
        return opcije.stream().map(opcija -> opcijaMapper.opcijaToOpcijaDto(opcija)).collect(Collectors.toList());
    }

    @Override
    public List<OpcijaKorisnikaDto> findAllForUser(Long id) {
        Optional<KorisnikoveKupljeneOpcije> opcije = korisnikKupljeneOpcijeRepository.findKorisnikoveKupljeneOpcijeByKorisnikId(id);
        return opcije.stream().map(opcija -> opcijaMapper.opcijaKorisnikaToNovaKorisnikovaKupljenaOpcijaDTO(opcija)).collect(Collectors.toList());
    }

    @Override
    public List<OpcijaDto> findByStockAndDateAndStrike(String ticker, LocalDateTime datumIstekaVazenja, Double strikePrice) {

        List<Opcija> opcije = this.opcijaRepository.findByStockAndDateAndStrike(ticker,datumIstekaVazenja,strikePrice);//(page-1)*6
        return opcije.stream().map(opcija -> opcijaMapper.opcijaToOpcijaDto(opcija)).collect(Collectors.toList());
    }

    @Override
    @Transactional//izdvajamo opciju i akciju jer su nezavisne promene
    public KorisnikoveKupljeneOpcije izvrsiOpciju(Long opcijaId, Long userId) {
                                                                                                        //moze ih biti vise pa uzimamo prvu neiskoriscenu
        KorisnikoveKupljeneOpcije korisnikKupljenaOpcija = korisnikKupljeneOpcijeRepository.findFirstByOpcijaIdAndKorisnikIdAndIskoriscenaFalse(opcijaId, userId).orElse(null);
        Opcija opcija = opcijaRepository.findById(opcijaId).orElse(null);
        Korisnik korisnik = korisnikRepository.findById(userId).orElse(null);

        if(korisnik == null || korisnikKupljenaOpcija == null || opcija == null || opcija.getOpcijaStanje().equals(OpcijaStanje.EXPIRED))//nema ni jednu konkretnu kupljenu opciju
            return null;


        GlobalQuote globalQuote = akcijaRepository.findFirstByTicker(opcija.getTicker()).orElse(null);

        if(globalQuote == null)
            return null;

        globalQuote.getPrice();

        korisnikKupljenaOpcija.setAkcijaTickerCenaPrilikomIskoriscenja(new BigDecimal(globalQuote.getPrice()));
        korisnikKupljenaOpcija.setIskoriscena(true);

        return korisnikKupljeneOpcijeRepository.save(korisnikKupljenaOpcija);
    }


    @Override
    public OpcijaStanje proveriStanjeOpcije(Long opcijaId){

        Opcija opcija = opcijaRepository.findById(opcijaId).orElse(null);

        if(opcija == null || opcija.getOpcijaStanje() == null)
            return null;

        if (opcija.getOpcijaStanje().equals(OpcijaStanje.EXPIRED))
            return OpcijaStanje.EXPIRED;

        GlobalQuote akcija = akcijaRepository.findFirstByTicker(opcija.getTicker()).orElse(null);

        if(akcija == null)
            return null;

        OpcijaStanje opcijaStanje = null;

        if(opcija.getStrikePrice() > akcija.getPrice() && opcija.getOptionType().equals(OpcijaTip.PUT))
            opcijaStanje = OpcijaStanje.IN_THE_MONEY;
        else if(opcija.getStrikePrice() < akcija.getPrice() && opcija.getOptionType().equals(OpcijaTip.PUT))
            opcijaStanje = OpcijaStanje.OUT_OF_MONEY;

        if(opcija.getStrikePrice() < akcija.getPrice() && opcija.getOptionType().equals(OpcijaTip.CALL))
            opcijaStanje = OpcijaStanje.IN_THE_MONEY;
        else if(opcija.getStrikePrice() > akcija.getPrice() && opcija.getOptionType().equals(OpcijaTip.CALL))
            opcijaStanje = OpcijaStanje.OUT_OF_MONEY;

        if(opcijaStanje == null)
            return OpcijaStanje.AT_THE_MONEY;

        return opcijaStanje;
    }
    @Override
    public OpcijaDto save(NovaOpcijaDto novaOpcijaDto) {

        Opcija opcija = opcijaMapper.novaOpcijaDtoToOpcija(novaOpcijaDto);
        opcija = opcijaRepository.save(opcija);

        return opcijaMapper.opcijaToOpcijaDto(opcija);
    }

    //CITA IZ CACHE A AKO NE POSTOJI ONDA IZ BAZE
    @Override
    //optional je ili objekat ili null(ako ne postoji u bazi)
    public OpcijaDto findById(Long id) {
        Cache cache = cacheManager.getCache("opcijeCache");

        if (cache != null) {
            Map<Object, Object> cacheMap = (Map<Object, Object>) cache.getNativeCache();

            if (cacheMap != null) {
                for (Map.Entry<Object, Object> entry : cacheMap.entrySet()) {
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof Opcija && key.equals(id)) {
                        log.info("findById iz cache");
                        return opcijaMapper.opcijaToOpcijaDto((Opcija) value);
                    }
                }
            }
        }
        return opcijaMapper.opcijaToOpcijaDto(opcijaRepository.findById(id).orElse(null));//ne postoji cache
    }


    private boolean firstTimeFetch = false;

    //UBACUJE U CACHE KAD GOD SE AZURIRA
    @Override
    @Scheduled(fixedRate = 60000)//poseban thread obradjuje
    @Transactional//sve promene nad bazom se upisuju u bazu tek kada se metoda uspesno zavrsi,ako dodje do izuzetka radi se roll back
    public void azuirajPostojeceOpcije() throws IOException {


        if(!firstTimeFetch) {
            List<GlobalQuote> globalQuotes = akcijaRepository.saveAll(fetchAllGlobalQuote());
            if(globalQuotes.size() == 0)
                return;
            firstTimeFetch = true;
        }
        List<Opcija> noveAzuriraneOpcije = fetchAllOptionsForAllTickers();
        List<Opcija> postojeceOpcije = opcijaRepository.findAll();


        for(Opcija o:noveAzuriraneOpcije){
            Optional<Opcija> postojecaOpcija = postojeceOpcije.stream()
                    .filter(opcija -> opcija.getContractSymbol().equals(o.getContractSymbol()))
                    .findFirst();

            //moze i brisanje svih opcija pa ponovno dodavanje
            /////////////////////////////////////////////////////
            //azuriramo postojece opcije
            postojecaOpcija.ifPresent(postojeca ->{
                postojeca.setStrikePrice(o.getStrikePrice());
                postojeca.setLastPrice(o.getLastPrice());
                postojeca.setBid(o.getBid());
                postojeca.setContractSymbol(o.getContractSymbol());
                postojeca.setOptionType(o.getOptionType());
                postojeca.setAsk(o.getAsk());
                postojeca.setTicker(o.getTicker());
                postojeca.setChange(o.getChange());
                postojeca.setPercentChange(o.getPercentChange());
                postojeca.setInTheMoney(o.isInTheMoney());
                postojeca.setImpliedVolatility(o.getImpliedVolatility());
                postojeca.setOpenInterest(o.getOpenInterest());
                postojeca.setContractSize(o.getContractSize());
                postojeca.setExpiration(o.getExpiration());

                postojeca.setDatumIstekaVazenja(o.getDatumIstekaVazenja());

                LocalDateTime trenutnoVreme = LocalDateTime.now();

                if(o.getDatumIstekaVazenja().isBefore(trenutnoVreme)) {
                    postojeca.setOpcijaStanje(OpcijaStanje.EXPIRED);
                    //postojeca.setIstaIstorijaGroupId();
                }
                GlobalQuote akcija = akcijaRepository.findFirstByTicker(postojeca.getTicker()).orElse(null);

                postojeca.izracunajIzvedeneVrednosti(izvedeneVrednostiUtil,akcija);

                //azurirana opcija                                                      //id                            obj
                cacheManager.getCache("opcijeCache").put(opcijaRepository.save(postojecaOpcija.get()).getId(),postojecaOpcija.get());
            });
            //nova opcija
            if(!postojecaOpcija.isPresent()) {
                GlobalQuote akcija = akcijaRepository.findFirstByTicker(o.getTicker()).orElse(null);

                o.izracunajIzvedeneVrednosti(izvedeneVrednostiUtil,akcija);

                cacheManager.getCache("opcijeCache").put(opcijaRepository.save(o).getId(),o);
            }
        }

    }

    @Override
    public Map<String, List<OpcijaDto>> findPutsAndCallsByStockTicker(String ticker) {
        Map<String, List<OpcijaDto>> resultMap = new HashMap<>();
        List<OpcijaDto> callsList = new ArrayList<>();
        List<OpcijaDto> putsList = new ArrayList<>();

        try {
            // Korišćenje fetchOptionsForTicker metode za dobijanje OptionChainResponse objekta
            OptionChainResponse optionChainResponse = fetchOptionsForTicker(ticker);

            // Pretpostavka da OptionChainResponse i povezane klase već imaju strukturu koja odgovara JSON-u
            optionChainResponse.getOptionChain().getResult().forEach(result -> {
                result.getOptions().forEach(option -> {
                    option.getCalls().forEach(call -> callsList.add(convertToOpcijaDto(call, result.getUnderlyingSymbol(), OpcijaTip.CALL)));
                    option.getPuts().forEach(put -> putsList.add(convertToOpcijaDto(put, result.getUnderlyingSymbol(), OpcijaTip.PUT)));
                });
            });

            resultMap.put("calls", callsList);
            resultMap.put("puts", putsList);
        } catch (Exception e) {
            log.error("Error during fetching options for ticker {}: {}", ticker, e.getMessage());
            // Možete logovati grešku ili obavestiti korisnika na odgovarajući način
        }

        return resultMap;
    }



    public OptionChainResponse fetchOptionsForTicker(String ticker) {
        // Kreiranje HTTP klijenta
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet("https://query1.finance.yahoo.com/v6/finance/options/" + ticker);

            // Slanje HTTP GET zahteva
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                // Provera statusnog koda odgovora
                if (response.getStatusLine().getStatusCode() == 200) {
                    // Dobijanje JSON stringa iz odgovora
                    String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");

                    // Inicijalizacija ObjectMapper-a za deserijalizaciju JSON-a
                    ObjectMapper objectMapper = new ObjectMapper();

                    // Deserijalizacija JSON-a u OptionChainResponse objekat
                    return objectMapper.readValue(jsonResponse, OptionChainResponse.class);
                } else {
                    // Obrada grešaka (npr. ako statusni kod nije 200)
                    throw new RuntimeException("Failed to fetch options for ticker " + ticker + ". Status code: " + response.getStatusLine().getStatusCode());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during HTTP request to Yahoo Finance API", e);
        }
    }


    private OpcijaDto convertToOpcijaDto(OptionChainResponse.Contract contract, String ticker, OpcijaTip optionType) {
        OpcijaDto dto = new OpcijaDto();
        dto.setTicker(ticker); // Setovanje tickera
        dto.setContractSymbol(contract.getContractSymbol()); // Simbol ugovora
        dto.setStrikePrice(contract.getStrike()); // Cena izvršenja
        dto.setCurrency(contract.getCurrency()); // Valuta
        dto.setLastPrice(contract.getLastPrice()); // Poslednja cena
        dto.setChange(contract.getChange()); // Promena
        dto.setPercentChange(contract.getPercentChange()); // Procentualna promena
        dto.setVolume(contract.getVolume()); // Volumen
        dto.setOpenInterest(contract.getOpenInterest()); // Otvoreni interes
        dto.setBid(contract.getBid()); // Ponuda
        dto.setAsk(contract.getAsk()); // Potražnja
//        dto.setContractSize(contract.getContractSize()); // Veličina ugovora, string tipa "REGULAR" , ovamo trazite tipa long ??
        dto.setExpiration(contract.getExpiration()); // Datum isteka
        dto.setLastTradeDate(contract.getLastTradeDate()); // Datum poslednje trgovine
        dto.setImpliedVolatility(contract.getImpliedVolatility()); // Implicitna volatilnost
        dto.setInTheMoney(contract.isInTheMoney()); // Da li je u novcu
        dto.setOptionType(optionType); // Tip opcije (CALL ili PUT)

        // Pretpostavka je da neki datumi poput `settlementDate` i `datumIstekaVazenja` trebaju biti konvertovani
        // iz UNIX timestampa u `LocalDateTime`. Ako to nije potrebno, ove linije mogu biti izostavljene ili prilagođene.
        if (contract.getExpiration() > 0) {
            dto.setSettlementDate(LocalDateTime.ofEpochSecond(contract.getExpiration(), 0, ZoneOffset.UTC));
            dto.setDatumIstekaVazenja(LocalDateTime.ofEpochSecond(contract.getExpiration(), 0, ZoneOffset.UTC));
        }

        return dto;
    }


    @Override
    public Map<String, List<OpcijaDto>> findPutsAndCallsByStockTickerAndExpirationDate(String ticker, Date startOfDay, Date endOfDay) {
        Map<String, List<OpcijaDto>> resultMap = new HashMap<>();
        List<OpcijaDto> callsList = new ArrayList<>();
        List<OpcijaDto> putsList = new ArrayList<>();

        try {
            Map<String, List<OpcijaDto>> response = findPutsAndCallsByStockTicker(ticker);
            List<OpcijaDto> allCalls = response.get("calls");
            List<OpcijaDto> allPuts = response.get("puts");

            for (OpcijaDto call : allCalls) {
                Date expirationDate = new Date(call.getExpiration());
                log.info(startOfDay + " " + expirationDate + " " + endOfDay);
                log.info(expirationDate.after(startOfDay) && expirationDate.before(endOfDay) ? "true" : "false");
                if (expirationDate.after(startOfDay) && expirationDate.before(endOfDay)) {
                    callsList.add(call);
                }
            }

            for (OpcijaDto put : allPuts) {
                Date expirationDate = new Date(put.getExpiration());
                if (expirationDate.after(startOfDay) && expirationDate.before(endOfDay)) {
                    putsList.add(put);
                }
            }

            resultMap.put("calls", callsList);
            resultMap.put("puts", putsList);
        } catch (Exception e) {
            log.error("Error during fetching options for ticker {}: {}", ticker, e.getMessage());
        }

        return resultMap;
    }

    @Override
    public Map<String, List<OpcijaDto>> classifyOptions(String ticker) {
        Map<String, List<OpcijaDto>> resultMap = new HashMap<>();
        List<OpcijaDto> itmList = new ArrayList<>();
        List<OpcijaDto> otmList = new ArrayList<>();

        try {
            // Dohvatamo sve opcije za zadati ticker
            Map<String, List<OpcijaDto>> allOptions = findPutsAndCallsByStockTicker(ticker);

            // Spajamo sve put i call opcije u jednu listu
            List<OpcijaDto> allOptionsList = new ArrayList<>();
            allOptionsList.addAll(allOptions.getOrDefault("calls", Collections.emptyList()));
            allOptionsList.addAll(allOptions.getOrDefault("puts", Collections.emptyList()));

            // Klasifikujemo svaku opciju kao ITM ili OTM
            for (OpcijaDto opcija : allOptionsList) {
                if (opcija.isInTheMoney()) {
                    itmList.add(opcija);
                } else {
                    otmList.add(opcija);
                }
            }

            resultMap.put("ITM", itmList);
            resultMap.put("OTM", otmList);
        } catch (Exception e) {
            log.error("Error during classification of options for ticker {}: {}", ticker, e.getMessage());
        }

        return resultMap;
    }

    @Override
    public boolean novaOpcijaKorisnika(OpcijaKorisnikaDto opcijaKorisnikaDto) {

        if(korisnikKupljeneOpcijeRepository.save(opcijaMapper.opcijaKorisnikaDtoToNovaKorisnikovaKupljenaOpcija(opcijaKorisnikaDto)) == null){
            return false;
        }
        return true;
    }


}
