package rs.edu.raf.opcija.dto;

import lombok.Data;
import rs.edu.raf.opcija.model.OpcijaStanje;
import rs.edu.raf.opcija.model.OpcijaTip;

import java.time.LocalDateTime;

@Data
public class OpcijaDto {

    //KA FRONT
    private Long id;

    //KA FRONT
    private String ticker;//stockSymbol odnosno kompanija cije se opcije(ugovori) prodaju ili kupuju odnosno cije se akcije prodaju ili kupuju opcijom

    //KA FRONT
    private String contractSymbol;//ticker_nekiIdUgovora

    //KA FRONT
    private double strikePrice;//cena izvrsenja opcije odnosno cena akcija opcije koja je dogovorena i nezavisna je od trenutnaCenaOsnovneAkcije

    //KA FRONT
    private double impliedVolatility;

    //KA FRONT
    private double openInterest;//koliko ih je zainteresovano za ovu opciju

    private LocalDateTime settlementDate;

    //KA FRONT
    private OpcijaTip optionType;


    private long ukupanBrojIzdatihAkcijaKompanije;
    private long trenutnaCenaOsnovneAkcijeKompanije;//stockPrice(currentAssetPrice)
    private long brojUgovora;//broj ugovora koje obuhvata opcija!


    //1 OPCIJA SADRZI VISE ISTIH UGOVORA,NE KUPUJE SE OPCIJA VEC NJENI UGOVORI

    //////////////////////////////////////////
    //izvedene vrednosti na osnovu relevantnih formula i cene osnovne akcije

    private LocalDateTime datumIstekaVazenja;//datum kada opcija istice izveden iz expiration

    private long vrednostPozicije;

    private OpcijaStanje opcijaStanje;

    private long marketCap;//trzisna vrednost kompanije
    private long maintenanceMargin;//minimalni iznos na racunu vlasnika opcije
    private long theta;
    private long vrednostOpcijeBlackScholes;//teorijska vrednost opcije ali se zapravo cena opcije formira na osnovu ponude i potraznje
    //////////////////////////////////////////
    //POLJA SA YAHOO APIJA
    private String currency;
    private double lastPrice;//cena po kojoj poslednji put kupljena odnosno prodana opcija

    //KA FRONT
    private double change;

    //KA FRONT
    private double percentChange;

    //KA FRONT
    private double bid;//trenutna najvisa ponuda za kupovinu opcije

    //KA FRONT
    private double ask;//najniza ponuda za prodaju opcije

    //KA FRONT
    private long contractSize;//broj akcija koje jedan ugovor opcije obuhvata!


    private long expiration;//broj sekundi od 1970
    private long lastTradeDate;
    private boolean inTheMoney;
    private long volume;

}
