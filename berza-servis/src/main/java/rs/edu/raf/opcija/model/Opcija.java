package rs.edu.raf.opcija.model;

import jakarta.persistence.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.opcija.servis.IzvedeneVrednostiUtil;

import java.time.*;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

//opcija je ugovor za vise akcija odnosno 1 opcija N akcija kompanije

//akcija je deo vlasnistva nad kompanijom
//cena akcije se moze menjati vremenom
//akcije se prodaju ili kupuju od drugih investitora ili kompanija
//profit od akcije moze biti kapitalni dobitak(prodaja akcija) ili dividende(profit firme koji se deli akcionarima)


//i sama opcija se kupuje,prodaje ili iskoriscava
//kada se opcija kupi to znaci da se broj akcija te opcije kupuje ili prodaje bilo kada do odredjenog datuma isteka opcije po dogovorenoj ceni
//ako je put opcija onda vlasnik put opcije mora imati u trenutku izvrsenja put opcije akcije koje prodaje po unapred dogovorenoj ceni
//ako je call opcija onda vlasnik call opcije mora platiti akcije u trenutku izvrsenja call opcije

//call opcija treba da se izvrsava kada stvarna cena akcija poraste iznad cene izvrsenja akcija call opcije,pa kada se izvrsi onda se vlasnik da kupi akcije po nizoj ceni call opcije pa ce odmah da ih proda po stvarnoj
public class Opcija{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    //za grupisanje po istoriji i dohvatanje istorije
    private Long istaIstorijaGroupId;


    @Column(nullable = false)
    private String ticker;//stockSymbol odnosno kompanija cije se opcije(ugovori) prodaju ili kupuju odnosno cije se akcije prodaju ili kupuju opcijom

    @Column(nullable = false)
    private String contractSymbol;//ticker_nekiIdUgovora


    @Column
    //opcija ulazi u istoriju ako joj se promeni cena ili istekne datum
    private double strikePrice;//cena izvrsenja opcije odnosno cena akcija opcije koja je dogovorena i nezavisna je od trenutnaCenaOsnovneAkcije

    private double impliedVolatility;
    private double openInterest;//koliko ih je zainteresovano za ovu opciju
    private LocalDateTime settlementDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)//obavezno da ne bi enum bio broj u bazi
    private OpcijaTip optionType;


    //private long ukupanBrojIzdatihAkcijaKompanije;
    //private double trenutnaCenaOsnovneAkcijeKompanije;//stockPrice(currentAssetPrice)
    private long brojUgovora;//broj ugovora koje obuhvata opcija!


    //1 OPCIJA SADRZI VISE ISTIH UGOVORA,NE KUPUJE SE OPCIJA VEC NJENI UGOVORI


    //IZVEDENE VREDNOSTI NA OSNOVU RELEVANTNE FORMULE I CENE OSNOVNE AKCIJE
    //////////////////////////////////////////
    private LocalDateTime datumIstekaVazenja;//datum kada opcija istice izveden iz expiration

    private long vrednostPozicije;

    //@Column(nullable = false)
    @Enumerated(EnumType.STRING)//obavezno da ne bi enum bio broj u bazi
    private OpcijaStanje opcijaStanje;

    private double marketCap;//trzisna vrednost kompanije
    private double maintenanceMargin;//minimalni iznos na racunu vlasnika opcije
    private double theta;
    private double vrednostOpcijeBlackScholes;//teorijska vrednost opcije ali se zapravo cena opcije formira na osnovu ponude i potraznje
    //////////////////////////////////////////
    //POLJA SA YAHOO APIJA
    private String currency;
    private double lastPrice;//cena po kojoj poslednji put kupljena odnosno prodana opcija
    private double change;
    private double percentChange;
    private double bid;//trenutna najvisa ponuda za kupovinu opcije
    private double ask;//najniza ponuda za prodaju opcije
    private long contractSize;//broj akcija koje jedan ugovor opcije obuhvata!
    private long expiration;//broj sekundi od 1970
    private long lastTradeDate;
    private boolean inTheMoney;
    private long volume;


    public void izracunajIzvedeneVrednosti(IzvedeneVrednostiUtil izvedeneVrednostiUtil, GlobalQuote globalQuote){

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        validateRequiredDataBeforeDeriving(this,validator);

        vrednostOpcijeBlackScholes = izvedeneVrednostiUtil.calculateBlackScholesValue(globalQuote.getPrice(),
                strikePrice,
                impliedVolatility,
                expiration);

        marketCap = globalQuote.getPrice()*globalQuote.getSharesOutstanding();
        theta =  (optionType.equals(OpcijaTip.CALL)?izvedeneVrednostiUtil.calculateThetaCall((double) globalQuote.getPrice(), strikePrice, 0.05,impliedVolatility, (double) expiration):izvedeneVrednostiUtil.calculateThetaPut((double) globalQuote.getPrice(), strikePrice, 0.05,impliedVolatility, (double) expiration));
        maintenanceMargin = izvedeneVrednostiUtil.calculateMaintenanceMargin(marketCap,0.2);

    }
    private void validateRequiredDataBeforeDeriving(Opcija option, Validator validator) {

        Set<ConstraintViolation<Opcija>> violations;

        violations = validator.validate(option);
        if (!violations.isEmpty()) {
            //throw new Exception("Validacija nije uspela: " + violations.toString());
            //throw new RuntimeException("Validacija nije uspela: " + violations.toString());
        }
    }
}
