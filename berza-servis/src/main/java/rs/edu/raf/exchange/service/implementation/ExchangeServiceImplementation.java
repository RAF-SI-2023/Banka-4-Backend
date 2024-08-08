package rs.edu.raf.exchange.service.implementation;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import rs.edu.raf.annotations.GeneratedLegacyCode;
import rs.edu.raf.exchange.dto.ExchangeDTO;
import rs.edu.raf.exchange.mapper.ExchangeMapper;
import rs.edu.raf.exchange.model.Exchange;
import rs.edu.raf.exchange.repository.ExchangeRepository;
import rs.edu.raf.exchange.service.ExchangeService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class ExchangeServiceImplementation implements ExchangeService {

    ExchangeRepository exchangeRepository;
    ExchangeMapper exchangeMapper;

    /*@Value("${iexCloud.apiKey}")
    private String iexCloudApiKey;

    private static final String IEX_CLOUD_BASE_URL = "https://api.iex.cloud/v1/";

    public String getExchangeInfo(String exchangeSymbol) {
        String apiUrl = IEX_CLOUD_BASE_URL + "/stock/" + exchangeSymbol + "/exchange";


        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("token", iexCloudApiKey);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);


        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch exchange info from IEX Cloud API");
        }
    }*/
    @Override
    public ExchangeDTO getExchangeByExchangeName(String name) {
        Exchange exchange = exchangeRepository.findByExchangeName(name);
        return exchangeMapper.exchangeToExchangeDTO(exchange);
    }

    @Override
    public List<ExchangeDTO> getAllExchanges() {
        List<Exchange> exchanges = exchangeRepository.findAll();
        List<ExchangeDTO> dtos = new ArrayList<>();
        for (Exchange e : exchanges) {
            ExchangeDTO dto = exchangeMapper.exchangeToExchangeDTO(e);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<ExchangeDTO> getExchangesByCurrency(String currency) {
       List<Exchange> exchanges = exchangeRepository.findAllByCurrency(currency);
        List<ExchangeDTO> dtos = new ArrayList<>();
        for (Exchange e : exchanges) {
            ExchangeDTO dto = exchangeMapper.exchangeToExchangeDTO(e);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<ExchangeDTO> getOpenExchanges() {
        List<Exchange> exchanges = exchangeRepository.findOpenExchanges(String.valueOf(LocalTime.now(ZoneId.systemDefault())));
        List<ExchangeDTO> dtos = new ArrayList<>();
        for (Exchange e : exchanges) {
            ExchangeDTO dto = exchangeMapper.exchangeToExchangeDTO(e);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<ExchangeDTO> getExchangesByPolity(String polity) {
        List<Exchange> exchanges = exchangeRepository.findAllByPolity(polity);
        List<ExchangeDTO> dtos = new ArrayList<>();
        for (Exchange e : exchanges) {
            ExchangeDTO dto = exchangeMapper.exchangeToExchangeDTO(e);
            dtos.add(dto);
        }
        return dtos;
    }


    /*@Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void updateChangesFromCSV(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1); // -1 to include empty values

                String exchangeName = data[0].trim();
                Exchange existingExchange = exchangeRepository.findByExchangeName(exchangeName);

                if (existingExchange == null) {
                    Exchange exchange = new Exchange();
                    exchange.setExchangeName(exchangeName);
                    exchange.setExchangeAcronym(data[1].trim());
                    exchange.setExchangeMICCode(data[2].trim());
                    exchange.setPolity(data[3].trim());
                    exchange.setCurrency(data[4].trim());
                    exchange.setTimeZone(ZoneId.of(data[5].trim()));

                    String openTimeStr = data[6].trim();
                    String closeTimeStr = data[7].trim();

                    if (!openTimeStr.isEmpty()) {
                        exchange.getExchangeSchedule().setOpenTime((openTimeStr));
                    }

                    if (!closeTimeStr.isEmpty()) {
                        exchange.getExchangeSchedule().setCloseTime((closeTimeStr));
                    }

                    exchangeRepository.save(exchange);
                } else {
                    existingExchange.setExchangeAcronym(data[1].trim());
                    existingExchange.setExchangeMICCode(data[2].trim());
                    existingExchange.setPolity(data[3].trim());
                    existingExchange.setCurrency(data[4].trim());
                    existingExchange.setTimeZone(ZoneId.of(data[5].trim()));

                    String openTimeStr = data[6].trim();
                    String closeTimeStr = data[7].trim();

                    if (!openTimeStr.isEmpty()) {
                        existingExchange.getExchangeSchedule().setOpenTime((openTimeStr));
                    }

                    if (!closeTimeStr.isEmpty()) {
                        existingExchange.getExchangeSchedule().setCloseTime((openTimeStr));
                    }

                    exchangeRepository.save(existingExchange);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error importing exchanges from CSV: " + e.getMessage(), e);
        }
    }*/

    @GeneratedLegacyCode
    public ZonedDateTime getCurrentTimeForExchange(String exchangeName) {
        Exchange exchange = exchangeRepository.findByExchangeName(exchangeName);
        if (exchange == null) {
            throw new IllegalArgumentException("Exchange not found: " + exchangeName);
        }

        ZoneId timeZone = exchange.getTimeZone();
        return ZonedDateTime.now(timeZone);
    }

    @GeneratedLegacyCode
    public boolean isDaylightSavingTime(String exchangeName) {
        Exchange exchange = exchangeRepository.findByExchangeName(exchangeName);
        if (exchange == null) {
            throw new IllegalArgumentException("Exchange not found: " + exchangeName);
        }

        ZoneId timeZone = exchange.getTimeZone();
        LocalDateTime now = LocalDateTime.now();
        return timeZone.getRules().isDaylightSavings(Instant.from(now));
    }

   /* @Scheduled(cron = "0 0 0 * * *") // Svaki dan u ponoć
    public void updateExchangeData() {
        // Logika za ažuriranje podataka o berzama
        List<Exchange> exchanges = exchangeRepository.findAll();
        for (Exchange exchange : exchanges) {
            // Implementirati logiku ažuriranja podataka za svaku berzu
            // Na primer, poziv IEX Cloud API-ja za dobijanje ažurnih informacija
            // Možete ažurirati informacije o radnom vremenu, letnjem vremenu, praznicima, itd.
            // Primer:
            // exchange.setOpenTime(...);
            // exchange.setCloseTime(...);
            // exchange.setDaylightSavingTime(...);
            // exchange.setHolidays(...);
            // exchangeRepository.save(exchange);
        }
    }*/



}
