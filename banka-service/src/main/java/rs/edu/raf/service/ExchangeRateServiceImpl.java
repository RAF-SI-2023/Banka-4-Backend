package rs.edu.raf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.model.dto.ExchangeRateResponseDto;
import rs.edu.raf.model.entities.ExchangeRate;
import rs.edu.raf.model.entities.racun.ExchangeInvoice;
import rs.edu.raf.model.mapper.ExchangeRateMapper;
import rs.edu.raf.repository.ExchangeRateRepository;
import rs.edu.raf.repository.racun.InvoiceRepository;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService{

    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateMapper exchangeRateMapper;
    private final String apiUrl = "https://v6.exchangerate-api.com/v6/4bf14e8ddbdbfe05d9022143/latest/EUR";
    private final List<String> allowedCurrencies = List.of("RSD","EUR", "CHF", "USD", "GBP", "JPY", "CAD", "AUD");
    private InvoiceRepository invoiceRepository;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository, ExchangeRateMapper exchangeRateMapper,
                                   InvoiceRepository invoiceRepository){
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateMapper = exchangeRateMapper;
        this.invoiceRepository = invoiceRepository;
        init();
    }

    private void init(){
        saveExchangeRates();
    }



    @Scheduled(cron = "0 5 8 * * *")
    private void scheduledSaveExchange(){
        saveExchangeRates();
    }

    public void saveExchangeRates(){
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> exchangeRatesResponse = restTemplate.getForObject(apiUrl, Map.class);

        if(exchangeRatesResponse != null && exchangeRatesResponse.containsKey("conversion_rates")){
            Map<String, Object> conversionRates = (Map<String, Object>) exchangeRatesResponse.get("conversion_rates");
            saveRates(conversionRates);
        }
    }

    private void saveRates(Map<String, Object> conversionRates){
    
        exchangeRateRepository.deleteAll();
        conversionRates.forEach((currencyCode, rate) -> {
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setCurrencyCode(currencyCode);
            exchangeRate.setRate(new BigDecimal(rate.toString()));
            exchangeRateRepository.save(exchangeRate);
            //System.out.println(exchangeRateRepository.save(exchangeRate));
        });
    }


    public List<ExchangeRateResponseDto> getAllExchangeRates(){
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findAll();

        return exchangeRates.stream().map(exchangeRateMapper::exchangeRateToExchangeRateResponseDto).toList();
    }

    private boolean isAllowedCurrency(String oldValuteCurrencyCode, String newValuteCurrencyCode){

        if(!this.allowedCurrencies.contains(oldValuteCurrencyCode.toUpperCase())
                || !this.allowedCurrencies.contains(newValuteCurrencyCode.toUpperCase())){
            return false;
        }

        return true;
    }

    public BigDecimal convert(String oldValuteCurrencyCode, String newValuteCurrencyCode, BigDecimal oldValuteAmount){

        Optional<ExchangeRate> oldOptionalExchangeRate = exchangeRateRepository.findByCurrencyCode(oldValuteCurrencyCode);
        Optional<ExchangeRate> newOptionalExchangeRate = exchangeRateRepository.findByCurrencyCode(newValuteCurrencyCode);

        BigDecimal oldAmount = oldOptionalExchangeRate.get().getRate();
        BigDecimal newAmount = newOptionalExchangeRate.get().getRate();

        BigDecimal provision = BigDecimal.valueOf(0.995);

        BigDecimal finalAmount = oldValuteAmount.divide(oldAmount, new MathContext(2, RoundingMode.HALF_DOWN)).multiply(newAmount).multiply(provision);

        return finalAmount;
    }

    

    @Override
    public List<ExchangeInvoice> listInvoicesByCurrency(String currency) {
        return invoiceRepository.findExchangeInvoicesBySenderCurrency(currency);
    }

    public BigDecimal exchangeRate(String oldValuteCurrencyCode, String newValuteCurrencyCode){

        Optional<ExchangeRate> oldOptionalExchangeRate = exchangeRateRepository.findByCurrencyCode(oldValuteCurrencyCode);
        Optional<ExchangeRate> newOptionalExchangeRate = exchangeRateRepository.findByCurrencyCode(newValuteCurrencyCode);

        BigDecimal oldAmount = oldOptionalExchangeRate.get().getRate();
        BigDecimal newAmount = newOptionalExchangeRate.get().getRate();

        return new BigDecimal("1.0").divide(oldAmount,new MathContext(2, RoundingMode.HALF_DOWN)).multiply(newAmount);
    }
}
