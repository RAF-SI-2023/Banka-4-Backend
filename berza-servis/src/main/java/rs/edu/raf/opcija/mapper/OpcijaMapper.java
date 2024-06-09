package rs.edu.raf.opcija.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.edu.raf.opcija.dto.NovaOpcijaDto;
import rs.edu.raf.opcija.dto.OpcijaDto;
import rs.edu.raf.opcija.dto.OpcijaKorisnikaDto;
import rs.edu.raf.opcija.model.*;
import rs.edu.raf.opcija.repository.GlobalQuoteRepository;
import rs.edu.raf.opcija.servis.IzvedeneVrednostiUtil;
import rs.edu.raf.opcija.servis.util.GlobalQuoteApiMap;
import rs.edu.raf.opcija.servis.util.OptionYahooApiMap;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class OpcijaMapper {

    @Autowired
    private IzvedeneVrednostiUtil izvedeneVrednostiUtil;

    @Autowired
    private GlobalQuoteRepository globalQuoteRepository;

    //OD FRONTA
    public Opcija novaOpcijaDtoToOpcija(NovaOpcijaDto novaOpcijaDto){

        Opcija opcija = new Opcija();


        return opcija;
    }
    //KA FRONTU
    public OpcijaDto opcijaToOpcijaDto(Opcija opcija){

        if(opcija == null)
            return null;

        OpcijaDto opcijaDto = new OpcijaDto();

        opcijaDto.setId(opcija.getId());
        opcijaDto.setStrikePrice(opcija.getStrikePrice());
        opcijaDto.setLastPrice(opcija.getLastPrice());
        opcijaDto.setBid(opcija.getBid());
        opcijaDto.setContractSymbol(opcija.getContractSymbol());
        opcijaDto.setOptionType(opcija.getOptionType());
        opcijaDto.setAsk(opcija.getAsk());
        opcijaDto.setTicker(opcija.getTicker());
        opcijaDto.setChange(opcija.getChange());
        opcijaDto.setPercentChange(opcija.getPercentChange());
        opcijaDto.setImpliedVolatility(opcija.getImpliedVolatility());
        opcijaDto.setOpenInterest(opcija.getOpenInterest());
        opcijaDto.setContractSize(opcija.getContractSize());

        opcijaDto.setInTheMoney(opcija.isInTheMoney());
        opcijaDto.setExpiration(opcija.getExpiration());
        //opcijaDto.setDatumIstekaVazenja(LocalDateTime.ofInstant(Instant.ofEpochMilli(opcija.getExpiration()),ZoneId.systemDefault()));
        opcijaDto.setDatumIstekaVazenja(LocalDateTime.ofInstant(Instant.ofEpochSecond(opcija.getExpiration()),ZoneOffset.systemDefault()));


        return opcijaDto;
    }

    public Opcija yahooOpcijaToOpcija(OptionYahooApiMap optionYahooApiMap){

        if(optionYahooApiMap == null)
            return null;

        Opcija opcija = new Opcija();
        GlobalQuote akcija = globalQuoteRepository.findFirstByTicker(optionYahooApiMap.getTicker()).orElse(null);

        opcija.setStrikePrice(optionYahooApiMap.getStrike());
        opcija.setLastPrice(optionYahooApiMap.getLastPrice());
        opcija.setBid(optionYahooApiMap.getBid());
        opcija.setContractSymbol(optionYahooApiMap.getContractSymbol());
        opcija.setOptionType(optionYahooApiMap.getOpcijaTip());
        opcija.setAsk(optionYahooApiMap.getAsk());
        opcija.setTicker(optionYahooApiMap.getTicker());
        opcija.setChange(optionYahooApiMap.getChange());
        opcija.setPercentChange(optionYahooApiMap.getPercentChange());
        opcija.setInTheMoney(optionYahooApiMap.getInTheMoney());
        opcija.setImpliedVolatility(optionYahooApiMap.getImpliedVolatility());
        opcija.setOpenInterest(optionYahooApiMap.getOpenInterest());
        opcija.setContractSize(optionYahooApiMap.getContractSize().equals("REGULAR")?100:0);
        opcija.setExpiration(optionYahooApiMap.getExpiration());

        //opcija.setDatumIstekaVazenja(LocalDateTime.ofInstant(Instant.ofEpochMilli(opcija.getExpiration()),ZoneId.systemDefault()));
        opcija.setDatumIstekaVazenja(LocalDateTime.ofInstant(Instant.ofEpochSecond(opcija.getExpiration()),ZoneOffset.systemDefault()));


        LocalDateTime trenutnoVreme = LocalDateTime.now();

        if(opcija.getDatumIstekaVazenja().isBefore(trenutnoVreme)) {
            opcija.setOpcijaStanje(OpcijaStanje.EXPIRED);
            //opcija.setIstaIstorijaGroupId();
        }else if(akcija != null){
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
                opcijaStanje = OpcijaStanje.AT_THE_MONEY;

            opcija.setOpcijaStanje(opcijaStanje);
        }

        //POTREBNI PARAMETRI ZA IZVEDENE VREDNOSTI
        //trenutnaCenaOsnovneAkcijeKompanije//na alphavantage
        //strikePrice
        //impliedVolatility
        //expiration
        //ukupanBrojIzdatihAkcijaKompanije//na alphavantage

        opcija.izracunajIzvedeneVrednosti(izvedeneVrednostiUtil,akcija);

        return opcija;
    }

    public GlobalQuote globalQuoteApiToGlobalQuote(GlobalQuoteApiMap globalQuoteApiMap){

        GlobalQuote globalQuote = new GlobalQuote();

        globalQuote.setTicker(globalQuoteApiMap.getSymbol());
        globalQuote.setChange(globalQuoteApiMap.getChange());
        globalQuote.setHigh(globalQuoteApiMap.getHigh());
        globalQuote.setChangePercent(globalQuoteApiMap.getChangePercent());
        globalQuote.setOpen(globalQuoteApiMap.getOpen());
        globalQuote.setPreviousClose(globalQuoteApiMap.getPreviousClose());
        globalQuote.setPrice(globalQuoteApiMap.getPrice());
        globalQuote.setVolume(globalQuoteApiMap.getVolume());
        globalQuote.setSharesOutstanding(globalQuoteApiMap.getSharesOutstanding());

        return globalQuote;
    }

    public KorisnikoveKupljeneOpcije opcijaKorisnikaDtoToNovaKorisnikovaKupljenaOpcija(OpcijaKorisnikaDto opcijaKorisnikaDto){

        KorisnikoveKupljeneOpcije korisnikoveKupljeneOpcije = new KorisnikoveKupljeneOpcije();
        korisnikoveKupljeneOpcije.setOpcijaId(opcijaKorisnikaDto.getOpcijaId());
        korisnikoveKupljeneOpcije.setIskoriscena(false);
        korisnikoveKupljeneOpcije.setAkcijaId(opcijaKorisnikaDto.getAkcijaId());
        korisnikoveKupljeneOpcije.setAkcijaTickerCenaPrilikomIskoriscenja(opcijaKorisnikaDto.getAkcijaTickerCenaPrilikomIskoriscenja());
        korisnikoveKupljeneOpcije.setKorisnikId(opcijaKorisnikaDto.getKorisnikId());

        return korisnikoveKupljeneOpcije;
    }

}
