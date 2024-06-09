package rs.edu.raf.currency.servis.impl;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.currency.dto.CurrencyDTO;
import rs.edu.raf.currency.mapper.CurrencyMapper;
import rs.edu.raf.currency.model.Currency;
import rs.edu.raf.currency.repository.CurrencyRepository;
import rs.edu.raf.currency.servis.CurrencyService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


@Service
public class CurrencyServiceImpl implements CurrencyService {

    CurrencyRepository currencyRepository;
    CurrencyMapper currencyMapper;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
    }

    @Override
    public CurrencyDTO getCurrencyByCurrencyCode(String currencyCode) {
        Currency c = currencyRepository.findById(currencyCode).orElse(null);
        CurrencyDTO dto = currencyMapper.currencyToCurrencyDTO(c);
        return dto;
    }

    @Override
    public List<CurrencyDTO> getAllCurrencies() {
        List<Currency> currencies = currencyRepository.findAll();
        List<CurrencyDTO> dtos = new ArrayList<>();
        for (Currency c : currencies) {
            CurrencyDTO dto = currencyMapper.currencyToCurrencyDTO(c);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<CurrencyDTO> getCurrenciesInPolity(String polity) {
        List<Currency> currencies = currencyRepository.findAllByPolity(polity);
        List<CurrencyDTO> dtos = new ArrayList<>();
        for (Currency c : currencies) {
            CurrencyDTO dto = currencyMapper.currencyToCurrencyDTO(c);
            dtos.add(dto);
        }
        return dtos;
    }


    @PostConstruct
    @Scheduled(cron = "0 0 10 * * *")
    private void updateCurrencies() {

        List<Currency> currencies = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://www.alphavantage.co/physical_currency_list/")).build();
        HttpResponse<String> response = null;         // FVHPQRIRKBYHSTJU
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        String body = response.body();
        String[] lines = body.substring(body.indexOf("\n")+1).split("\n");
        for (String l : lines) {
            String[] c = l.split(",");
            Currency currency = new Currency();
            currency.setCurrencyCode(c[0]);
            currency.setCurrencyName(c[1]);
            try {
                currency.setCurrencySymbol(java.util.Currency.getInstance(c[0]).getSymbol()); //with locale? needs country codes
            } catch (NullPointerException | IllegalArgumentException e) {
                currency.setCurrencySymbol(c[0]);
            }
            currency.setPolity(c[0].substring(0,2)); //country code api?
            currencies.add(currency);
        }
        currencyRepository.saveAll(currencies);
    }
}
