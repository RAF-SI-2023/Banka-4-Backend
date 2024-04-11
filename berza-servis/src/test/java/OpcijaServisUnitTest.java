import io.restassured.RestAssured;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.opcija.model.GlobalQuote;
import rs.edu.raf.opcija.model.OpcijaTip;
import rs.edu.raf.opcija.servis.IzvedeneVrednostiUtil;
import rs.edu.raf.opcija.servis.util.FinansijaApiUtil;
import rs.edu.raf.opcija.servis.util.GlobalQuoteApiMap;
import rs.edu.raf.opcija.servis.util.JsonParserUtil;
import rs.edu.raf.opcija.servis.util.OptionYahooApiMap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import static io.restassured.RestAssured.given;
import static org.apache.logging.log4j.ThreadContext.isEmpty;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
//@RunWith(MockitoJUnitRunner.class)
//@PrepareForTest(FinansijaApiUtil.class)
public class OpcijaServisUnitTest {

    //@InjectMocks
    //FinansijaApiUtil finansijaApiUtil;

    @Test
    public void testYahooApiDeserialization() throws IOException {
        String json = "{" +
                "\"optionChain\": {" +
                "\"result\": [" +
                "{" +
                "\"underlyingSymbol\": \"AAPL\"," +
                "\"expirationDates\": [" +
                "1711065600," +
                "1711584000," +
                "1712275200," +
                "1712880000," +
                "1713484800," +
                "1714089600," +
                "1715904000," +
                "1718928000," +
                "1721347200," +
                "1723766400," +
                "1726790400," +
                "1729209600," +
                "1731628800," +
                "1734652800," +
                "1737072000," +
                "1742515200," +
                "1750377600," +
                "1758240000," +
                "1766102400," +
                "1768521600," +
                "1781740800" +
                "]," +
                "\"strikes\": [" +
                "100.0," +
                "105.0," +
                "110.0," +
                "115.0," +
                "120.0," +
                "125.0," +
                "130.0," +
                "135.0," +
                "140.0," +
                "145.0," +
                "146.0," +
                "147.0," +
                "148.0," +
                "149.0," +
                "150.0," +
                "152.5," +
                "155.0," +
                "157.5," +
                "160.0," +
                "162.5," +
                "165.0," +
                "167.5," +
                "170.0," +
                "172.5," +
                "175.0," +
                "177.5," +
                "180.0," +
                "182.5," +
                "185.0," +
                "187.5," +
                "190.0," +
                "192.5," +
                "195.0," +
                "197.5," +
                "200.0," +
                "202.5," +
                "205.0," +
                "210.0," +
                "215.0," +
                "220.0," +
                "225.0," +
                "230.0," +
                "235.0," +
                "240.0," +
                "245.0," +
                "255.0," +
                "260.0," +
                "265.0" +
                "]," +
                "\"hasMiniOptions\": false," +
                "\"quote\": {" +
                "\"language\": \"en-US\"," +
                "\"region\": \"US\"," +
                "\"quoteType\": \"EQUITY\"," +
                "\"triggerable\": true," +
                "\"quoteSourceName\": \"Nasdaq Real Time Price\"," +
                "\"currency\": \"USD\"," +
                "\"shortName\": \"Apple Inc.\"," +
                "\"sharesOutstanding\": 15441899520," +
                "\"bookValue\": 4.793," +
                "\"fiftyDayAverage\": 183.2858," +
                "\"fiftyDayAverageChange\": -10.665802," +
                "\"fiftyDayAverageChangePercent\": -0.05819219," +
                "\"twoHundredDayAverage\": 183.8347," +
                "\"twoHundredDayAverageChange\": -11.214706," +
                "\"twoHundredDayAverageChangePercent\": -0.061004296," +
                "\"marketCap\": 2665580593152," +
                "\"forwardPE\": 24.108938," +
                "\"priceToBook\": 36.01502," +
                "\"sourceInterval\": 15," +
                "\"exchangeTimezoneName\": \"America/New_York\"," +
                "\"exchangeTimezoneShortName\": \"EDT\"," +
                "\"gmtOffSetMilliseconds\": -14400000," +
                "\"priceHint\": 2," +
                "\"preMarketChange\": 1.1000061," +
                "\"preMarketChangePercent\": 0.6372414," +
                "\"preMarketTime\": 1710749371," +
                "\"preMarketPrice\": 173.72," +
                "\"longName\": \"Apple Inc.\"," +
                "\"financialCurrency\": \"USD\"," +
                "\"averageDailyVolume3Month\": 58545398," +
                "\"averageDailyVolume10Day\": 76014170," +
                "\"fiftyTwoWeekLowChange\": 18.470001," +
                "\"fiftyTwoWeekLowChangePercent\": 0.119818375," +
                "\"fiftyTwoWeekRange\": \"154.15 - 199.62\"," +
                "\"fiftyTwoWeekHighChange\": -27.0," +
                "\"fiftyTwoWeekHighChangePercent\": -0.13525699," +
                "\"fiftyTwoWeekLow\": 154.15," +
                "\"fiftyTwoWeekHigh\": 199.62," +
                "\"market\": \"us_market\"," +
                "\"esgPopulated\": false," +
                "\"tradeable\": false," +
                "\"marketState\": \"PRE\"," +
                "\"regularMarketChangePercent\": -0.21965599," +
                "\"regularMarketDayRange\": \"170.29 - 172.62\"," +
                "\"regularMarketPreviousClose\": 173.0," +
                "\"bid\": 173.08," +
                "\"ask\": 173.09," +
                "\"bidSize\": 12," +
                "\"askSize\": 11," +
                "\"messageBoardId\": \"finmb_24937\"," +
                "\"fullExchangeName\": \"NasdaqGS\"," +
                "\"regularMarketPrice\": 172.62," +
                "\"regularMarketTime\": 1710532802," +
                "\"regularMarketChange\": -0.38000488," +
                "\"regularMarketOpen\": 171.0," +
                "\"regularMarketDayHigh\": 172.62," +
                "\"regularMarketDayLow\": 170.29," +
                "\"regularMarketVolume\": 121752699," +
                "\"exchange\": \"NMS\"," +
                "\"dividendDate\": 1707955200," +
                "\"earningsTimestamp\": 1706824800," +
                "\"earningsTimestampStart\": 1714647540," +
                "\"earningsTimestampEnd\": 1714996800," +
                "\"trailingAnnualDividendRate\": 0.95," +
                "\"trailingPE\": 26.804346," +
                "\"trailingAnnualDividendYield\": 0.0054913294," +
                "\"epsTrailingTwelveMonths\": 6.44," +
                "\"epsForward\": 7.16," +
                "\"epsCurrentYear\": 6.56," +
                "\"priceEpsCurrentYear\": 26.314024," +
                "\"exchangeDataDelayedBy\": 0," +
                "\"symbol\": \"AAPL\"" +
                "},"+"\"options\": [" +
                "{" +
                "\"expirationDate\": 1711065600," +
                "\"hasMiniOptions\": false," +
                "\"calls\": [" +
                "{" +
                "\"contractSymbol\": \"AAPL240322C00100000\"," +
                "\"strike\": 100.0," +
                "\"currency\": \"USD\"," +
                "\"lastPrice\": 73.01," +
                "\"change\": 0.0," +
                "\"percentChange\": 0.0," +
                "\"volume\": 5," +
                "\"openInterest\": 0," +
                "\"bid\": 0.0," +
                "\"ask\": 0.0," +
                "\"contractSize\": \"REGULAR\"," +
                "\"expiration\": 1711065600," +
                "\"lastTradeDate\": 1710445704," +
                "\"impliedVolatility\": 1.0000000000000003E-5," +
                "\"inTheMoney\": true" +
                "}" +
                "],\"puts\":[]" +
                "}" +
                "]" +
                "}"+
                "],"+
                "\"error\":null"+
                "}}";

        CloseableHttpResponse responseMock = mock(CloseableHttpResponse.class);
        CloseableHttpClient httpClientMock = mock(CloseableHttpClient.class);
        HttpGet httpGet = mock(HttpGet.class);
        HttpEntity httpEntity = mock(HttpEntity.class);

        when(httpClientMock.execute(httpGet)).thenReturn(responseMock);

        InputStream mockInputStream = new ByteArrayInputStream(json.getBytes());
        when(responseMock.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(mockInputStream);

        FinansijaApiUtil finansijaApiUtil = new FinansijaApiUtil();
        finansijaApiUtil.setJsonParserUtil(new JsonParserUtil());

        finansijaApiUtil.setHttpClient(httpClientMock);
        finansijaApiUtil.setResponse(responseMock);
        finansijaApiUtil.setHttpGet(httpGet);
        //nebitno je sta se prosledjuje ali bitno je da je 1 parametar da bi se test izvrsio jednom
        List<OptionYahooApiMap> result = finansijaApiUtil.fetchOptionsFromYahooApi(Arrays.asList("AAPL"));
        OptionYahooApiMap firstOption = result.get(0);

        assertNotNull(result);

        assertNotNull("contractSymbol ne sme biti null", firstOption.getContractSymbol());
        assertNotNull(firstOption.getStrike());
        assertNotNull("currency ne sme biti null", firstOption.getCurrency());
        assertNotNull(firstOption.getLastPrice());
        assertNotNull(firstOption.getChange());
        assertNotNull(firstOption.getPercentChange());
        assertNotNull(firstOption.getBid());
        assertNotNull(firstOption.getAsk());
        assertNotNull("contractSize ne sme biti null", firstOption.getContractSize());
        assertNotNull(firstOption.getExpiration());
        assertNotNull(firstOption.getLastTradeDate());
        assertNotNull(firstOption.getImpliedVolatility());
        assertNotNull(firstOption.getInTheMoney());
        assertNotNull(firstOption.getTicker());
        assertNotNull(firstOption.getOpcijaTip());

        assertEquals("AAPL240322C00100000", firstOption.getContractSymbol());//
        assertEquals(Double.parseDouble("100.0"), firstOption.getStrike().doubleValue(),0.0001);
        assertEquals("USD", firstOption.getCurrency());
        assertEquals(Double.parseDouble("73.01"), firstOption.getLastPrice().doubleValue(),0.0001);//
        assertEquals(Double.parseDouble("0.0"), firstOption.getChange().doubleValue(),0.0001);//
        assertEquals(Double.parseDouble("0.0"), firstOption.getPercentChange().doubleValue(),0.0001);//
        assertEquals(Double.parseDouble("0.0"), firstOption.getBid().doubleValue(),0.0001);
        assertEquals(Double.parseDouble("0.0"), firstOption.getAsk().doubleValue(),0.0001);
        assertEquals("REGULAR", firstOption.getContractSize());
        assertEquals(Long.parseLong("1711065600"), firstOption.getExpiration().doubleValue(),0.0001);
        assertEquals(Long.parseLong("1710445704"), firstOption.getLastTradeDate().doubleValue(),0.0001);
        assertEquals(Double.parseDouble("1.0000000000000003E-5"), firstOption.getImpliedVolatility().doubleValue(),0.0001);
        assertEquals(Boolean.parseBoolean("true"), firstOption.getInTheMoney());
        assertEquals("AAPL", firstOption.getTicker());
        assertEquals(OpcijaTip.CALL, firstOption.getOpcijaTip());
    }


    @Test
    public void testPolygonApiDeserialization() throws IOException {

        String json = "{" +
                "\"results\": [" +
                "{" +
                "\"ticker\": \"A\"," +
                "\"name\": \"Agilent Technologies Inc.\"," +
                "\"market\": \"stocks\"," +
                "\"locale\": \"us\"," +
                "\"primary_exchange\": \"XNYS\"," +
                "\"type\": \"CS\"," +
                "\"active\": true," +
                "\"currency_name\": \"usd\"," +
                "\"cik\": \"0001090872\"," +
                "\"composite_figi\": \"BBG000C2V3D6\"," +
                "\"share_class_figi\": \"BBG001SCTQY4\"," +
                "\"last_updated_utc\": \"2024-03-15T00:00:00Z\"" +
                "}" +
                "]" +
                "}";


        FinansijaApiUtil finansijaApiUtil = new FinansijaApiUtil();
        finansijaApiUtil.setJsonParserUtil(new JsonParserUtil());

        CloseableHttpResponse responseMock = mock(CloseableHttpResponse.class);
        CloseableHttpClient httpClientMock = mock(CloseableHttpClient.class);
        HttpGet httpGet = mock(HttpGet.class);
        HttpEntity entity = mock(HttpEntity.class);

        finansijaApiUtil.setResponse(responseMock);
        finansijaApiUtil.setHttpClient(httpClientMock);
        finansijaApiUtil.setHttpGet(httpGet);

        when(httpClientMock.execute(httpGet)).thenReturn(responseMock);

        InputStream mockInputStream = new ByteArrayInputStream(json.getBytes());
        when(responseMock.getEntity()).thenReturn(entity);

        //u svaki when je ulazni parametar mock objekat a return ti predefinises
        //a svaki when ulazni parametar odnosno mock mora biti setovan u testnoj klasi
        //najbitniji je poslednji when jer on odredjuje sta ce biti ulazni parametri u testnu metodu
        when(entity.getContent()).thenReturn(mockInputStream);

        List<String> result = finansijaApiUtil.fetchTickerNames();
        assertNotNull("ticker objekat ne sme biti null",result.get(0));
        assertEquals("A",result.get(0));

    }
    //sve sto se mockuje ocemo da nasa testna funkcija ne zavisi od toga odnosno moze i da se izbaci iz testiranja
    //jer zapravo ako mockujes metodu i predefinises sta vraca odnosno input za sledecu testnu metodu mozes i direktno ubaciti input u sledecu testnu metodu
    //zapravo testiramo samo i samo kod odnosno metode koje su deo naseg sistema
    //svaka metoda je wraper zapravo jer zove druge metode,ali jedino sto je bitno je da se izbace iz svih metoda pozivi izvan naseg sistema odnosno da se mockuju
    //sto znaci da ako imas metodu u sistemu koja samo zove druge metode u tvom sistemu onda nista neces mockovati
    //isto tako ako imas metodu u sistemu koja zove samo zove druge metode koje su izvan tvog sistema onda ces sve mockovati a onda nema ni potrebe za testiranjem


    @Test
    public void testAlphaVantageApiDeserialization() throws IOException, ParseException {

        String json = "{" +
                "\"Global Quote\": {" +
                "\"01. symbol\": \"AAPL\"," +
                "\"02. open\": \"171.7600\"," +
                "\"03. high\": \"173.0500\"," +
                "\"04. low\": \"170.0600\"," +
                "\"05. price\": \"172.2800\"," +
                "\"06. volume\": \"71160138\"," +
                "\"07. latest trading day\": \"2024-03-22\"," +
                "\"08. previous close\": \"171.3700\"," +
                "\"09. change\": \"0.9100\"," +
                "\"10. change percent\": \"0.5310%\"" +
                "}" +
                "}";

        String json2 =  "{" +
                "\"Symbol\": \"AAPL\"," +
                "\"AssetType\": \"Common Stock\"," +
                "\"Name\": \"Apple Inc\"," +
                "\"Description\": \"Apple Inc. is an American multinational technology company that specializes in consumer electronics, computer software, and online services. Apple is the world's largest technology company by revenue (totalling $274.5 billion in 2020) and, since January 2021, the world's most valuable company. As of 2021, Apple is the world's fourth-largest PC vendor by unit sales, and fourth-largest smartphone manufacturer. It is one of the Big Five American information technology companies, along with Amazon, Google, Microsoft, and Facebook.\"," +
                "\"CIK\": \"320193\"," +
                "\"Exchange\": \"NASDAQ\"," +
                "\"Currency\": \"USD\"," +
                "\"Country\": \"USA\"," +
                "\"Sector\": \"TECHNOLOGY\"," +
                "\"Industry\": \"ELECTRONIC COMPUTERS\"," +
                "\"Address\": \"ONE INFINITE LOOP, CUPERTINO, CA, US\"," +
                "\"FiscalYearEnd\": \"September\"," +
                "\"LatestQuarter\": \"2023-12-31\"," +
                "\"MarketCapitalization\": \"2660330373000\"," +
                "\"EBITDA\": \"130108998000\"," +
                "\"PERatio\": \"26.83\"," +
                "\"PEGRatio\": \"2.12\"," +
                "\"BookValue\": \"4.793\"," +
                "\"DividendPerShare\": \"0.95\"," +
                "\"DividendYield\": \"0.0056\"," +
                "\"EPS\": \"6.42\"," +
                "\"RevenuePerShareTTM\": \"24.65\"," +
                "\"ProfitMargin\": \"0.262\"," +
                "\"OperatingMarginTTM\": \"0.338\"," +
                "\"ReturnOnAssetsTTM\": \"0.212\"," +
                "\"ReturnOnEquityTTM\": \"1.543\"," +
                "\"RevenueTTM\": \"385706000000\"," +
                "\"GrossProfitTTM\": \"170782000000\"," +
                "\"DilutedEPSTTM\": \"6.42\"," +
                "\"QuarterlyEarningsGrowthYOY\": \"0.16\"," +
                "\"QuarterlyRevenueGrowthYOY\": \"0.021\"," +
                "\"AnalystTargetPrice\": \"201.28\"," +
                "\"AnalystRatingStrongBuy\": \"10\"," +
                "\"AnalystRatingBuy\": \"17\"," +
                "\"AnalystRatingHold\": \"12\"," +
                "\"AnalystRatingSell\": \"2\"," +
                "\"AnalystRatingStrongSell\": \"0\"," +
                "\"TrailingPE\": \"26.83\"," +
                "\"ForwardPE\": \"26.39\"," +
                "\"PriceToSalesRatioTTM\": \"6.9\"," +
                "\"PriceToBookRatio\": \"35.9\"," +
                "\"EVToRevenue\": \"6.99\"," +
                "\"EVToEBITDA\": \"20.19\"," +
                "\"Beta\": \"1.289\"," +
                "\"52WeekHigh\": \"199.37\"," +
                "\"52WeekLow\": \"155.15\"," +
                "\"50DayMovingAverage\": \"182.33\"," +
                "\"200DayMovingAverage\": \"183.71\"," +
                "\"SharesOutstanding\": \"15441900000\"," +
                "\"DividendDate\": \"2024-02-15\"," +
                "\"ExDividendDate\": \"2024-02-09\"" +
                "}";

        FinansijaApiUtil finansijaApiUtil = new FinansijaApiUtil();
        finansijaApiUtil.setJsonParserUtil(new JsonParserUtil());

        CloseableHttpResponse responseMock1 = mock(CloseableHttpResponse.class);
        CloseableHttpResponse responseMock2 = mock(CloseableHttpResponse.class);
        CloseableHttpClient httpClientMock = mock(CloseableHttpClient.class);
        HttpGet httpGet1 = mock(HttpGet.class);
        HttpGet httpGet2 = mock(HttpGet.class);
        HttpEntity entity1 = mock(HttpEntity.class);
        HttpEntity entity2 = mock(HttpEntity.class);

        finansijaApiUtil.setResponse(responseMock1);
        finansijaApiUtil.setResponseAlpha(responseMock2);
        finansijaApiUtil.setHttpClient(httpClientMock);
        finansijaApiUtil.setHttpGet(httpGet1);
        finansijaApiUtil.setHttpGetAlpha(httpGet2);

        when(httpClientMock.execute(httpGet1)).thenReturn(responseMock1);
        when(httpClientMock.execute(httpGet2)).thenReturn(responseMock2);

        InputStream mockInputStream1 = new ByteArrayInputStream(json.getBytes());
        InputStream mockInputStream2 = new ByteArrayInputStream(json2.getBytes());
        when(responseMock1.getEntity()).thenReturn(entity1);
        when(responseMock2.getEntity()).thenReturn(entity2);

        when(entity1.getContent()).thenReturn(mockInputStream1);
        when(entity2.getContent()).thenReturn(mockInputStream2);

        List<GlobalQuoteApiMap> result = finansijaApiUtil.fetchGlobalQuote(Arrays.asList("AAPL"));
        GlobalQuoteApiMap globalQuoteApiMap = result.get(0);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        assertNotNull(globalQuoteApiMap.getChangePercent());
        assertNotNull(globalQuoteApiMap.getLatestTradingDay());
        assertNotNull(globalQuoteApiMap.getSharesOutstanding());
        assertNotNull(globalQuoteApiMap.getSymbol());

        assertEquals("AAPL", globalQuoteApiMap.getSymbol());
        assertEquals("0.5310%", globalQuoteApiMap.getChangePercent());
        assertEquals(Double.parseDouble("0.9100"), globalQuoteApiMap.getChange());
        assertEquals(simpleDateFormat.parse("2024-03-22"), globalQuoteApiMap.getLatestTradingDay());
        assertEquals(Double.parseDouble("173.0500"), globalQuoteApiMap.getHigh());
        assertEquals(Double.parseDouble("170.0600"), globalQuoteApiMap.getLow());
        assertEquals(Double.parseDouble("171.7600"), globalQuoteApiMap.getOpen());
        assertEquals(Double.parseDouble("171.3700"), globalQuoteApiMap.getPreviousClose());
        assertEquals(Double.parseDouble("172.2800"), globalQuoteApiMap.getPrice());
        assertEquals(Long.parseLong("71160138"), globalQuoteApiMap.getVolume());

        assertEquals(Long.parseLong("15441900000"), globalQuoteApiMap.getSharesOutstanding());

    }

    @Test
    public void testGenerateOptionBlackScholes(){

        //istekla opcija ima vrednost 0
        double trenutnaCenaOsnovneAkcije = 100.0;
        double cenaIzvrsenjaOpcije = 90.0;
        double volatilnostCeneOsnovneAkcije = 0.2;
        double brojMilisecDoIstekaOpcije = 1711197431;

        IzvedeneVrednostiUtil izvedeneVrednostiUtil = new IzvedeneVrednostiUtil();
        //ne mockujes objekat koji ima funkciju za testiranje jer svi mockovani objekti imaju prazne implementacije funkcija

        double result = izvedeneVrednostiUtil.calculateBlackScholesValue(
                trenutnaCenaOsnovneAkcije, cenaIzvrsenjaOpcije,volatilnostCeneOsnovneAkcije ,
                (long) brojMilisecDoIstekaOpcije);

        //ti moras znati unapred koji je ispravan rezultat za testni input da bi mogao da proveris da li testna funkcija radi ispravno
        assertTrue(result == 0);

    }
}

