import io.restassured.RestAssured;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
public class OpcijaControllerUnitTest {


    @Test//https://query1.finance.yahoo.com/v6/finance/options/AAPL
    public void testIntegrationYahooApi(){
        RestAssured.baseURI = "https://query1.finance.yahoo.com";

        given()
                .when()
                .get("/v6/finance/options/AAPL")
                .then()
                .statusCode(200)
                .assertThat()
                .body(not(isEmpty()))
                .body("optionChain", notNullValue());
    }

    @Test//https://api.polygon.io/v3/reference/tickers?active=true&apiKey=sD9RXYv12OWqAVg8ovsgtVaI91l988Op
    public void testIntegrationPolygonApi(){

        RestAssured.baseURI = "https://api.polygon.io";

        given()
                .queryParam("active", "true")
                .queryParam("apiKey", "sD9RXYv12OWqAVg8ovsgtVaI91l988Op")
                .when()
                .get("/v3/reference/tickers")
                .then()
                .statusCode(200)
                .assertThat()
                .body(not(isEmpty()))
                .body("results", notNullValue());//samo na prvoj dubini polja objekta
    }

    @Test//https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=AAPL&apikey=A9OROHDG6WFRDS62
    public void testIntegrationAlphaVantage1(){

        RestAssured.baseURI = "https://www.alphavantage.co";

        given()
                .queryParam("function", "GLOBAL_QUOTE")
                .queryParam("symbol", "AAPL")
                .queryParam("apikey", "A9OROHDG6WFRDS62")
                .when()
                .get("/query")
                .then()
                .statusCode(200)
                .body(not(isEmpty()))
                .body("'Global Quote'", notNullValue());
    }
    @Test//https://www.alphavantage.co/query?function=OVERVIEW&symbol=AAPL&apikey=A9OROHDG6WFRDS62
    public void testIntegrationAlphaVantage2(){

        RestAssured.baseURI = "https://www.alphavantage.co";

        // Slanje GET zahteva na odgovarajući endpoint sa query parametrima
        given()
                .queryParam("function", "OVERVIEW")
                .queryParam("symbol", "AAPL")
                .queryParam("apikey", "A9OROHDG6WFRDS62")
                .when()
                .get("/query")
                .then()
                .statusCode(200)
                .body(not(isEmpty()))
                .body("Name", notNullValue())
                .body("Symbol", notNullValue())
                .body("SharesOutstanding", notNullValue());
    }
}

