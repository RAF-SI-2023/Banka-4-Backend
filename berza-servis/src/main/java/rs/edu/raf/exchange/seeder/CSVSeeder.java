package rs.edu.raf.exchange.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.edu.raf.exchange.model.Exchange;
import rs.edu.raf.exchange.model.ExchangeWorkingHours;
import rs.edu.raf.exchange.repository.ExchangeRepository;
import rs.edu.raf.exchange.repository.ExchangeWorkingHoursRepository;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CSVSeeder implements CommandLineRunner {
    private ExchangeRepository exchangeRepository;
    private ExchangeWorkingHoursRepository exchangeWorkingHoursRepository;

    public CSVSeeder(ExchangeRepository exchangeRepository, ExchangeWorkingHoursRepository exchangeWorkingHoursRepository) {
        this.exchangeRepository = exchangeRepository;
        this.exchangeWorkingHoursRepository = exchangeWorkingHoursRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Exchange> exchanges = createExchanges();
        exchangeRepository.deleteAll();
        exchangeRepository.saveAll(exchanges);
    }

    private Exchange createExchange(String exchangeName, String exchangeAcronym, String exchangeMICCode, String country,
                                    String currency, String timeZone, String openTime, String closeTime) {
        Exchange exchange = new Exchange();
        exchange.setExchangeName(exchangeName);
        exchange.setExchangeAcronym(exchangeAcronym);
        exchange.setExchangeMICCode(exchangeMICCode);
        exchange.setPolity(country);
        exchange.setCurrency(currency);
        exchange.setTimeZone(ZoneId.of(timeZone));

        ExchangeWorkingHours workingHours = new ExchangeWorkingHours();
        workingHours.setExchange(exchange);
        workingHours.setOpenTime(openTime);
        workingHours.setCloseTime(closeTime);

        LocalTime openLocalTime = LocalTime.parse(openTime);
        LocalTime preMarketOpenLocalTime = openLocalTime.minusHours(3);
        workingHours.setPreMarketOpenTime(preMarketOpenLocalTime.toString());
        workingHours.setPreMarketCloseTime(openTime);

        LocalTime closeLocalTime = LocalTime.parse(closeTime);
        LocalTime postMarketOpenLocalTime = closeLocalTime.plusHours(3);
        workingHours.setPostMarketOpenTime(closeTime);
        workingHours.setPostMarketCloseTime(postMarketOpenLocalTime.toString());


        exchange.setExchangeSchedule(workingHours);

        return exchange;
    }

    /*private List<String> getHolidays(){
        Map<String,Map<String,String>> holidaysWithExchanges = new HashMap<>();
        Map<String,String> holidaysWithDates = new HashMap<>();
        holidaysWithDates.put("Eid al-Fitr","Apr 08, 2024");
        holidaysWithDates.put("Ramadan Holiday","Apr 08, 2024");



    }*/

    private List<Exchange> createExchanges() {
        List<Exchange> exchanges = new ArrayList<>();

        exchanges.add(createExchange("Nasdaq", "NASDAQ", "XNAS", "USA", "USD", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Jakarta Futures Exchange (bursa Berjangka Jakarta)", "BBJ", "XBBJ", "Indonesia", "Indonesian Rupiah", "Asia/Jakarta", "09:00", "17:30"));
        exchanges.add(createExchange("Asx - Trade24", "SFE", "XSFE", "Australia", "Australian Dollar", "Australia/Melbourne", "10:00", "16:00"));
        exchanges.add(createExchange("Cboe Edga U.s. Equities Exchange Dark", "EDGADARK", "EDGD", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Clear Street", "CLST", "CLST", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Wall Street Access Nyc", "WABR", "WABR", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Marex Spectron Europe Limited - Otf", "MSEL OTF", "MSEL", "Ireland", "Euro", "Europe/Dublin", "08:00", "16:30"));
        exchanges.add(createExchange("Borsa Italiana Equity Mtf", "BITEQMTF", "MTAH", "Italy", "Euro", "Europe/Rome", "09:00", "17:25"));
        exchanges.add(createExchange("Clearcorp Dealing Systems India Limited - Astroid", "ASTROID", "ASTR", "India", "Indian Rupee", "Asia/Kolkata", "09:15", "15:30"));
        exchanges.add(createExchange("Memx Llc Equities", "MEMX", "MEMX", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Natixis - Systematic Internaliser", "NATX", "NATX", "France", "Euro", "Europe/Paris", "09:00", "17:30"));
        exchanges.add(createExchange("Currenex Ireland Mtf - Rfq", "CNX MTF", "ICXR", "Ireland", "Euro", "Europe/Dublin", "08:00", "16:30"));
        exchanges.add(createExchange("Neo Exchange - Neo-l (market By Order)", "NEO-L", "NEOE", "Canada", "Canadian Dollar", "America/Montreal", "09:30", "16:00"));
        exchanges.add(createExchange("Polish Trading Point", "PTP", "PTPG", "Poland", "Polish Zloty", "Europe/Warsaw", "09:00", "17:35"));
        exchanges.add(createExchange("Pfts Stock Exchange", "PFTS", "PFTS", "Ukraine", "Ukrainian Hryvnia", "Europe/Kiev", "10:00", "17:30"));
        exchanges.add(createExchange("Cboe Australia - Transferable Custody Receipt Market", "CHI-X", "CXAR", "Australia", "Australian Dollar", "Australia/Melbourne", "10:00", "16:00"));
        exchanges.add(createExchange("Essex Radez Llc", "GLPS", "GLPS", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("London Metal Exchange", "LME", "XLME", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Multi Commodity Exchange Of India Ltd.", "MCX", "XIMC", "India", "Indian Rupee", "Asia/Kolkata", "09:15", "15:30"));
        exchanges.add(createExchange("Cassa Di Compensazione E Garanzia Spa - Ccp Agricultural Commodity Derivatives", "CCGAGRIDER", "CGGD", "Italy", "Euro", "Europe/Rome", "09:00", "17:25"));
        exchanges.add(createExchange("Toronto Stock Exchange - Drk", "TSX DRK", "XDRK", "Canada", "Canadian Dollar", "America/Montreal", "09:30", "16:00"));
        exchanges.add(createExchange("Chicago Mercantile Exchange", "CME", "XCME", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Neo Connect", "NEO CONNECT", "NEOC", "Canada", "Canadian Dollar", "America/Montreal", "09:30", "16:00"));
        exchanges.add(createExchange("Ubs Ats", "UBS ATS", "UBSA", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Athens Exchange - Apa", "ATHEX APA", "AAPA", "Greece", "Euro", "Europe/Athens", "10:15", "17:20"));
        exchanges.add(createExchange("Bny Mellon - Systematic Internaliser", "BNYM", "BKLF", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Bank Of Montreal - London Branch - Systematic Internaliser", "BMO", "BMLB", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Financial Information Contributors Exchange", "FICONEX", "FICX", "Germany", "Euro", "Europe/Berlin", "08:00", "20:00"));
        exchanges.add(createExchange("Abn Amro Clearing Bank - Systematic Internaliser", "AACB SI", "ABNC", "Netherlands", "Euro", "Europe/Amsterdam", "09:00", "17:40"));
        exchanges.add(createExchange("Credit Industriel Et Commercial - Systematic Internaliser", "CIC", "CMCI", "France", "Euro", "Europe/Paris", "09:00", "17:30"));
        exchanges.add(createExchange("Posit Rfq", "RFQ", "XRFQ", "Ireland", "Euro", "Europe/Dublin", "08:00", "16:30"));
        exchanges.add(createExchange("Aquis Exchange Plc", "AQX", "AQXE", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Financial And Risk Transactions Services Ireland Limited - Fxall Rfs Mtf", "FRTSIL", "FXRS", "Ireland", "Euro", "Europe/Dublin", "08:00", "16:30"));
        exchanges.add(createExchange("Berenberg Fixed Income Uk - Systematic Internaliser", "BGFU", "BGFU", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("National Bank Financial Inc. - Systematic Internaliser", "NBF", "NBFL", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Jpbx", "JPBX", "JPBX", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Miax Pearl Llc", "MPRL", "MPRL", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Canadian Securities Exchange", "CSE LISTED", "XCNQ", "Canada", "Canadian Dollar", "America/Montreal", "09:30", "16:00"));
        exchanges.add(createExchange("Two Sigma Securities Llc", "SOHO", "SOHO", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Currenex Ldfx", "CX LDFX", "LCUR", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Puma Capital Llc - Options", "PUMA", "PUMX", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Virtual Auction Global Markets - Mtf", "VAGM MTF", "VAGM", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Miami International Holdings Inc.", "MIHI", "MIHI", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Cboe  Europe - Lis Service", "CBOE  LIS", "LISX", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Global Securities Exchange", "GSX", "XGSX", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Tw Sef Llc", "TWSEF", "TWSF", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Gtx Sef Llc", "GTX", "GTXS", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Freight Investor Services Limited", "FIS", "FISU", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Mercado De Valores De Buenos Aires S.a.", "MERVAL", "XMEV", "Argentina", "Argentine Peso", "America/Argentina/Buenos_Aires", "11:00", "17:00"));
        exchanges.add(createExchange("Electricity Day-ahead Market", "EXIST", "XEDA", "Turkey", "Turkish Lira", "Asia/Istanbul", "09:00", "17:30"));
        exchanges.add(createExchange("Emerging Markets Bond Exchange Limited", "EMBX", "EMBX", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Six Swiss Bilateral Trading Platform For Structured Otc Products", "SIX", "XBTR", "Switzerland", "Swiss Franc", "Europe/Zurich", "09:00", "17:30"));
        exchanges.add(createExchange("Minneapolis Grain Exchange", "MGE", "XMGE", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Svenska Handelsbanken Ab - Svex", "SVEX", "SVEX", "Sweden", "Swedish Krona", "Europe/Oslo", "09:00", "16:30"));
        exchanges.add(createExchange("Global Derivatives Exchange", "GDX", "XGDX", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Bnp Paribas Securities Services London Branch - Systematic Internaliser", "BP2S LB SI", "BSPL", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Cboe Europe Equities - European Equities (nl)", "CBOE EUROPE B.V.", "CCXE", "Netherlands", "Euro", "Europe/Amsterdam", "09:00", "17:40"));
        exchanges.add(createExchange("Bnp Paribas Sa - Systematic Internaliser", "BNPP SA SI", "BNPS", "France", "Euro", "Europe/Paris", "09:00", "17:30"));
        exchanges.add(createExchange("Tradegate Exchange - Systematic Internaliser", "TGAG", "TGSI", "Germany", "Euro", "Europe/Berlin", "08:00", "20:00"));
        exchanges.add(createExchange("Exane Bnp Paribas", "EXEU", "EXEU", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Liquidnet H20", "LQNT H20", "LIQH", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("New York Portfolio Clearing", "NYPC", "NYPC", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Gemma (gilt Edged Market Makers’association)", "GEMMA", "GEMX", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Hudson River Trading (hrt)", "HRT", "HRTF", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Santander Uk - Systematic Internaliser", "SNUK", "SNUK", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Aquis Exchange Plc Aquis - Eix Infrastructure Bond Market", "AQUIS-EIX", "EIXE", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Henex S.a.", "HENEX", "HEMO", "Greece", "Euro", "Europe/Athens", "10:15", "17:20"));
        exchanges.add(createExchange("Jane Street Financial Ltd - Systematic Internaliser", "JSF", "JSSI", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Securitised Derivatives Market", "SEDEX", "SEDX", "Italy", "Euro", "Europe/Rome", "09:00", "17:25"));
        exchanges.add(createExchange("Rivercross Dark", "RIVERX", "RICD", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Bank Polska Kasa Opieki S.a. - Systematic Internaliser", "PEKAO", "PKOP", "Poland", "Polish Zloty", "Europe/Warsaw", "09:00", "17:35"));
        exchanges.add(createExchange("Xtend", "XTND", "XTND", "Hungary", "Hungarian Forint", "Europe/Budapest", "09:00", "17:00"));
        exchanges.add(createExchange("The Green Stock Exchange - Acb Impact Markets", "GSE", "GRSE", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Susquehanna International Securities Limited - Systematic Internaliser", "SISI", "SISI", "Ireland", "Euro", "Europe/Dublin", "08:00", "16:30"));
        exchanges.add(createExchange("Banque Et Caisse D'epargne De L'etat Luxembourg - Bcee - Systematic Internaliser", "BCEE", "BCEE", "Luxembourg", "Euro", "Europe/Luxembourg", "09:00", "17:35"));
        exchanges.add(createExchange("Merrill Lynch International - Rfq - Systematic Internaliser", "MLI", "MLRQ", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Westpac Banking Corporation - Systematic Internaliser", "WBC SI", "WSIN", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Bulgarian Stock Exchange - Alternative Market", "BSE", "ABUL", "Bulgaria", "Bulgarian Lev", "Europe/Sofia", "10:10", "16:55"));
        exchanges.add(createExchange("Gfi Securities Llc - Creditmatch (latg)", "LATG", "LATG", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Italian Derivatives Market", "IDEM", "XDMI", "Italy", "Euro", "Europe/Rome", "09:00", "17:25"));
        exchanges.add(createExchange("Hudson River Trading", "HRT", "HRTX", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Nex Sef Mtf - Reset - Risk Mitigation Services", "NSL", "REST", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Banque Internationale A Luxembourg S.a. - Systematic Internaliser", "BIL", "BILU", "Luxembourg", "Euro", "Europe/Luxembourg", "09:00", "17:35"));
        exchanges.add(createExchange("Liquidnet Inc.", "LQNT", "LIUS", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Bnp Paribas Securities Services - Systematic Internaliser", "BP2S", "BPSX", "France", "Euro", "Europe/Paris", "09:00", "17:30"));
        exchanges.add(createExchange("Ice Endex European Gas Spot", "ICE ENDEX SPOT", "NDXS", "Netherlands", "Euro", "Europe/Amsterdam", "09:00", "17:40"));
        exchanges.add(createExchange("Societe Generale - Systematic Internaliser", "SG SI", "XSGA", "France", "Euro", "Europe/Paris", "09:00", "17:30"));
        exchanges.add(createExchange("Level Ats - Vwap Cross", "LEVEL", "EBXV", "United States", "United States Dollar", "America/New_York", "09:30", "16:00"));
        exchanges.add(createExchange("Wells Fargo Securities Europe S.a.", "WFSE", "WFSE", "France", "Euro", "Europe/Paris", "09:00", "17:30"));
        exchanges.add(createExchange("Bnp Paribas Sa London Branch - Systematic Internaliser", "BNPP SA LB SI", "BNPL", "United Kingdom", "British Pound Sterling", "Europe/London", "08:00", "16:00"));
        exchanges.add(createExchange("Warsaw Stock Exchange/indices", "GPWB", "WIND", "Poland", "Polish Zloty", "Europe/Warsaw", "09:00", "17:35"));

        return exchanges;
    }


    }
