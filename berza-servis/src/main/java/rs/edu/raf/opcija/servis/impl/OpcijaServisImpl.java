package rs.edu.raf.opcija.servis.impl;

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
import java.time.LocalDateTime;
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
    private GlobalQuoteRepository globalQuoteRepository;

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
    public List<Opcija> findAl() {
        return opcijaRepository.findAll();
    }

    @Override
    public List<OpcijaDto> findByStockAndDateAndStrike(String ticker, LocalDateTime datumIstekaVazenja, Double strikePrice) {

        List<Opcija> opcije = this.opcijaRepository.findByStockAndDateAndStrike(ticker,datumIstekaVazenja,strikePrice);//(page-1)*6
        return opcije.stream().map(opcija -> opcijaMapper.opcijaToOpcijaDto(opcija)).collect(Collectors.toList());
    }

    @Override
    @Transactional//izdvajamo opciju i akciju jer su nezavisne promene
    public KorisnikoveKupljeneOpcije izvrsiOpciju(Long opcijaId, Long userId) {

        //da bi izvrsio opciju, prethodno mora da je kupi
        //moze ih biti vise pa uzimamo prvu neiskoriscenu
        KorisnikoveKupljeneOpcije korisnikKupljenaOpcija = korisnikKupljeneOpcijeRepository.findFirstByOpcijaIdAndKorisnikIdAndIskoriscenaFalse(opcijaId, userId).orElse(null);
        Opcija opcija = opcijaRepository.findById(opcijaId).orElse(null);
        Korisnik korisnik = korisnikRepository.findById(userId).orElse(null);

        if(korisnik == null || korisnikKupljenaOpcija == null || opcija == null || opcija.getOpcijaStanje().equals(OpcijaStanje.EXPIRED))//nema ni jednu konkretnu kupljenu opciju
            return null;


        GlobalQuote globalQuote = globalQuoteRepository.findFirstByTicker(opcija.getTicker()).orElse(null);

        if(globalQuote == null)
            return null;

        korisnikKupljenaOpcija.setAkcijaTickerCenaPrilikomIskoriscenja(globalQuote.getPrice());
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

        GlobalQuote akcija = globalQuoteRepository.findFirstByTicker(opcija.getTicker()).orElse(null);

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

    @Override
    public Opcija save(Opcija opcija) {
        return opcijaRepository.save(opcija);
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


    private volatile boolean firstTimeFetch = false;

    //UBACUJE U CACHE KAD GOD SE AZURIRA
    @Override
    @Scheduled(fixedRate = 60000)//poseban thread obradjuje
    @Transactional//sve promene nad bazom se upisuju u bazu tek kada se metoda uspesno zavrsi,ako dodje do izuzetka radi se roll back
    public void azuirajPostojeceOpcije() throws IOException {


        if(!firstTimeFetch) {
            List<GlobalQuote> globalQuotes = globalQuoteRepository.saveAll(fetchAllGlobalQuote());
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
                GlobalQuote akcija = globalQuoteRepository.findFirstByTicker(postojeca.getTicker()).orElse(null);

                //defaultna
                if(akcija == null){
                    akcija = new GlobalQuote();
                    akcija.setPrice(100.0);
                    akcija.setVolume(100);
                    akcija.setPreviousClose(100);
                    akcija.setOpen(100);
                    akcija.setHigh(100);
                    akcija.setLow(100);
                    akcija.setTicker(postojeca.getTicker());
                    akcija.setSharesOutstanding(Long.valueOf(100000001));
                    akcija.setChangePercent("3%");
                    akcija.setVolume(71160138);
                    akcija.setChange(100);

                    globalQuoteRepository.save(akcija);
                }


                postojeca.izracunajIzvedeneVrednosti(izvedeneVrednostiUtil,akcija);

                //azurirana opcija                                                      //id                            obj
                cacheManager.getCache("opcijeCache").put(opcijaRepository.save(postojecaOpcija.get()).getId(),postojecaOpcija.get());
            });
            //nova opcija
            if(!postojecaOpcija.isPresent()) {
                GlobalQuote akcija = globalQuoteRepository.findFirstByTicker(o.getTicker()).orElse(null);

                //defaultna
                if(akcija == null){
                    akcija = new GlobalQuote();
                    akcija.setPrice(100.0);
                    akcija.setVolume(100);
                    akcija.setPreviousClose(100);
                    akcija.setOpen(100);
                    akcija.setHigh(100);
                    akcija.setLow(100);
                    akcija.setTicker(o.getTicker());
                    akcija.setSharesOutstanding(Long.valueOf(100000002));
                    akcija.setChangePercent("3%");
                    akcija.setVolume(71160138);
                    akcija.setChange(100);

                    globalQuoteRepository.save(akcija);
                }
                o.izracunajIzvedeneVrednosti(izvedeneVrednostiUtil,akcija);

                cacheManager.getCache("opcijeCache").put(opcijaRepository.save(o).getId(),o);
            }
        }

    }


}
