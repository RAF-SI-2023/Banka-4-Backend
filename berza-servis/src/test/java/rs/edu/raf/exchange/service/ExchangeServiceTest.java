package rs.edu.raf.exchange.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import rs.edu.raf.exchange.dto.ExchangeDTO;
import rs.edu.raf.exchange.mapper.ExchangeMapper;
import rs.edu.raf.exchange.model.Exchange;
import rs.edu.raf.exchange.repository.ExchangeRepository;
import rs.edu.raf.exchange.service.implementation.ExchangeServiceImplementation;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
@SpringBootTest
public class ExchangeServiceTest {
    @Mock
    private ExchangeRepository exchangeRepository;

    @Mock
    private ExchangeMapper exchangeMapper;

    @InjectMocks
    private ExchangeServiceImplementation exchangeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetExchangeByExchangeName() {
        String exchangeName = "NASDAQ";
        Exchange exchange = new Exchange();
        exchange.setExchangeName(exchangeName);

        ExchangeDTO expectedDTO = new ExchangeDTO();
        expectedDTO.setExchangeName(exchangeName);

        // Mock-ovanje repository metode
        when(exchangeRepository.findByExchangeName(exchangeName)).thenReturn(exchange);
        when(exchangeMapper.exchangeToExchangeDTO(exchange)).thenReturn(expectedDTO);

        // Pozivanje servisne metode
        ExchangeDTO resultDTO = exchangeService.getExchangeByExchangeName(exchangeName);

        // Provera rezultata
        Assertions.assertNotNull(resultDTO);
        Assertions.assertEquals(expectedDTO.getExchangeName(), resultDTO.getExchangeName());
    }

    @Test
    public void testGetAllExchanges() {

        List<Exchange> exchanges = new ArrayList<>();
        Exchange exchange1 = new Exchange();
        exchange1.setExchangeName("NYSE");
        Exchange exchange2 = new Exchange();
        exchange2.setExchangeName("LSE");
        exchanges.add(exchange1);
        exchanges.add(exchange2);

        List<ExchangeDTO> expectedDTOs = new ArrayList<>();
        expectedDTOs.add(new ExchangeDTO("NYSE"));
        expectedDTOs.add(new ExchangeDTO("LSE"));


        when(exchangeRepository.findAll()).thenReturn(exchanges);
        when(exchangeMapper.exchangeToExchangeDTO(any(Exchange.class))).thenReturn(new ExchangeDTO());


        List<ExchangeDTO> resultDTOs = exchangeService.getAllExchanges();


        Assertions.assertNotNull(resultDTOs);
        Assertions.assertEquals(expectedDTOs.size(), resultDTOs.size());
    }


    @Test
    public void testGetExchangesByCurrency_WithValidCurrency() {

        String currency = "USD";
        Exchange exchange1 = new Exchange();
        exchange1.setExchangeName("NYSE");
        exchange1.setCurrency(currency);
        Exchange exchange2 = new Exchange();
        exchange2.setExchangeName("LSE");
        exchange2.setCurrency(currency);

        List<Exchange> exchanges = new ArrayList<>();
        exchanges.add(exchange1);
        exchanges.add(exchange2);

        ExchangeDTO expectedDTO = new ExchangeDTO("NYSE");
        ExchangeDTO expectedDTO2 = new ExchangeDTO("LSE");


        when(exchangeRepository.findAllByCurrency(currency)).thenReturn(exchanges);
        when(exchangeMapper.exchangeToExchangeDTO(exchange1)).thenReturn(expectedDTO);
        when(exchangeMapper.exchangeToExchangeDTO(exchange2)).thenReturn(expectedDTO2);


        List<ExchangeDTO> resultDTOs = exchangeService.getExchangesByCurrency(currency);


        Assertions.assertNotNull(resultDTOs);
        Assertions.assertEquals(2, resultDTOs.size());
        Assertions.assertEquals(expectedDTO.getExchangeName(), resultDTOs.get(0).getExchangeName());
        Assertions.assertEquals(expectedDTO2.getExchangeName(), resultDTOs.get(1).getExchangeName());
    }

    @Test
    public void testGetExchangesByCurrency_WithInvalidCurrency() {

        String currency = "EUR";


        when(exchangeRepository.findAllByCurrency(currency)).thenReturn(new ArrayList<>());


        List<ExchangeDTO> resultDTOs = exchangeService.getExchangesByCurrency(currency);


        Assertions.assertNotNull(resultDTOs);
        Assertions.assertTrue(resultDTOs.isEmpty());
    }

    @Test
    public void testGetOpenExchanges_WithOpenExchange() {

        Exchange exchange = new Exchange();
        exchange.setExchangeName("NYSE");

        List<Exchange> openExchanges = new ArrayList<>();
        openExchanges.add(exchange);

        ExchangeDTO expectedDTO = new ExchangeDTO("NYSE");


        when(exchangeRepository.findOpenExchanges(anyString()))
                .thenReturn(openExchanges);
        when(exchangeMapper.exchangeToExchangeDTO(exchange)).thenReturn(expectedDTO);


        List<ExchangeDTO> resultDTOs = exchangeService.getOpenExchanges();


        Assertions.assertNotNull(resultDTOs);
        Assertions.assertEquals(1, resultDTOs.size());
        Assertions.assertEquals(expectedDTO.getExchangeName(), resultDTOs.get(0).getExchangeName());
    }

    @Test
    public void testGetOpenExchanges_WithNoOpenExchange() {

        LocalTime currentTime = LocalTime.of(23, 0);


        when(exchangeRepository.findOpenExchanges(String.valueOf(currentTime)))
                .thenReturn(new ArrayList<>());


        List<ExchangeDTO> resultDTOs = exchangeService.getOpenExchanges();


        Assertions.assertNotNull(resultDTOs);
        Assertions.assertTrue(resultDTOs.isEmpty());
    }


    @Test
    public void testGetExchangesByPolity_WithValidPolity() {

        String polity = "USA";
        Exchange exchange1 = new Exchange();
        exchange1.setExchangeName("NYSE");
        exchange1.setPolity(polity);
        Exchange exchange2 = new Exchange();
        exchange2.setExchangeName("NASDAQ");
        exchange2.setPolity(polity);

        List<Exchange> exchanges = new ArrayList<>();
        exchanges.add(exchange1);
        exchanges.add(exchange2);

        ExchangeDTO expectedDTO = new ExchangeDTO("NYSE");
        ExchangeDTO expectedDTO2 = new ExchangeDTO("NASDAQ");


        when(exchangeRepository.findAllByPolity(polity)).thenReturn(exchanges);
        when(exchangeMapper.exchangeToExchangeDTO(exchange1)).thenReturn(expectedDTO);
        when(exchangeMapper.exchangeToExchangeDTO(exchange2)).thenReturn(expectedDTO2);


        List<ExchangeDTO> resultDTOs = exchangeService.getExchangesByPolity(polity);


        Assertions.assertNotNull(resultDTOs);
        Assertions.assertEquals(2, resultDTOs.size());
        Assertions.assertEquals(expectedDTO.getExchangeName(), resultDTOs.get(0).getExchangeName());
        Assertions.assertEquals(expectedDTO2.getExchangeName(), resultDTOs.get(1).getExchangeName());
    }

    @Test
    public void testGetExchangesByPolity_WithInvalidPolity() {

        String polity = "Europe";


        when(exchangeRepository.findAllByPolity(polity)).thenReturn(new ArrayList<>());


        List<ExchangeDTO> resultDTOs = exchangeService.getExchangesByPolity(polity);


        Assertions.assertNotNull(resultDTOs);
        Assertions.assertTrue(resultDTOs.isEmpty());
    }




}
