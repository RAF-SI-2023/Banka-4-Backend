package rs.edu.raf.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.model.dto.ExchangeRateResponseDto;
import rs.edu.raf.model.entities.ExchangeRate;
import rs.edu.raf.model.mapper.ExchangeRateMapper;
import rs.edu.raf.repository.ExchangeRateRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceImplTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private ExchangeRateMapper exchangeRateMapper;

    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

//    @Test
//    void saveExchangeRates() {
//        Map<String, Object> conversionRates = new HashMap<>();
//        conversionRates.put("RSD", 123.45);
//        conversionRates.put("EUR", 1.0);
//        conversionRates.put("USD", 0.85);
//
//        when(exchangeRateRepository.save(any(ExchangeRate.class))).thenReturn(new ExchangeRate());
//
//        exchangeRateService.saveExchangeRates();
//
//        verify(exchangeRateRepository, times(3)).save(any(ExchangeRate.class));
//
//    }

    @Test
    void getAllExchangeRates() {

        List<ExchangeRate> exchangeRateList = List.of(
                new ExchangeRate(1L,"RSD", BigDecimal.valueOf(123.45)),
                new ExchangeRate(2L,"EUR", BigDecimal.valueOf(1.0)),
                new ExchangeRate(3L,"USD", BigDecimal.valueOf(0.85))
        ) ;

        when(exchangeRateRepository.findAll()).thenReturn(exchangeRateList);
        when(exchangeRateMapper.exchangeRateToExchangeRateResponseDto(any(ExchangeRate.class))).thenReturn(new ExchangeRateResponseDto());

        List<ExchangeRateResponseDto> result = exchangeRateService.getAllExchangeRates();

        assertEquals(exchangeRateList.size(), result.size());

        verify(exchangeRateMapper, times(exchangeRateList.size()))
                .exchangeRateToExchangeRateResponseDto(any(ExchangeRate.class));


    }


    @Test
    void convert() {
        ExchangeRate eurExchangeRate = new ExchangeRate(1L, "EUR", BigDecimal.valueOf(1.0));
        ExchangeRate usdExchangeRate = new ExchangeRate(2L, "USD", BigDecimal.valueOf(0.85));

        when(exchangeRateRepository.findByCurrencyCode("EUR")).thenReturn(Optional.of(eurExchangeRate));
        when(exchangeRateRepository.findByCurrencyCode("USD")).thenReturn(Optional.of(usdExchangeRate));

        BigDecimal oldValuteAmount = BigDecimal.valueOf(100);
        BigDecimal convertedAmount = exchangeRateService.convert("EUR", "USD", oldValuteAmount);

        BigDecimal expectedConvertedAmount = BigDecimal.valueOf(85.0).multiply(BigDecimal.valueOf(0.995));

        assertEquals(expectedConvertedAmount, convertedAmount);

    }
}