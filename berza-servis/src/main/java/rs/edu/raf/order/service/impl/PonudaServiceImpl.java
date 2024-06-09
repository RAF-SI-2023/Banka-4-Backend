package rs.edu.raf.order.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.order.dto.Banka3StockDTO;
import rs.edu.raf.order.dto.DodajPonuduDto;
import rs.edu.raf.order.dto.PonudaBanci3Dto;
import rs.edu.raf.order.dto.PonudaDTO;
import rs.edu.raf.order.model.MojaPonudaBanci3Dto;
import rs.edu.raf.order.model.MojePonudeBanci3;
import rs.edu.raf.order.model.Ponuda;
import rs.edu.raf.order.model.StranaPonudaDTO;
import rs.edu.raf.order.repository.MojePonudeBanci3Repository;
import rs.edu.raf.order.repository.PonudaRepository;
import rs.edu.raf.order.service.MojePonudeBanci3Service;
import rs.edu.raf.order.service.PonudaService;
import rs.edu.raf.order.service.mapper.PonudaMapper;


import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@Service
public class PonudaServiceImpl implements PonudaService {

    private PonudaMapper ponudaMapper;
    private PonudaRepository ponudaRepository;
    private ObjectMapper objectMapper;
    private MojePonudeBanci3Repository mojePonudeBanci3Repository;


    @Override
    public List<Banka3StockDTO> dohvatiStokoveBanke3() {

        String banka3StocksEndpoint = "https://banka-3-dev.si.raf.edu.rs/exchange-service/api/v1/otcTrade/getStocks";

        List<Banka3StockDTO> result = null;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getBanka3StocksRequest = HttpRequest.newBuilder()
                .uri(URI.create(banka3StocksEndpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> getBanka3StocksResponse = client.send(getBanka3StocksRequest, HttpResponse.BodyHandlers.ofString());

            result = objectMapper.readValue(getBanka3StocksResponse.body(), new TypeReference<List<Banka3StockDTO>>() {});
            return result;

        } catch (IOException | InterruptedException e) {
            System.out.println("ERROR IN PonudaServiceImpl" + e);
        }

        throw null;
    }

    @Override
    public List<MojePonudeBanci3> svePonudeKaBanci3() {
        return mojePonudeBanci3Repository.findAll();
    }

    @Override
    public void dodajPonudu(DodajPonuduDto ponudaDTO) {
        ponudaRepository.save(ponudaMapper.ponudaDTOToPonuda(ponudaDTO));
    }

    @Override
    public void dodajPonuduBanci3(PonudaBanci3Dto ponudaBanci3Dto) {
        MojePonudeBanci3 mojePonudeBanci3 = new MojePonudeBanci3(ponudaBanci3Dto.getTicker(),ponudaBanci3Dto.getAmount(),ponudaBanci3Dto.getPrice(),"U OBRADI");
        MojePonudeBanci3 mojePonudeBanci31 = mojePonudeBanci3Repository.save(mojePonudeBanci3);
        ponudaBanci3Dto.setIdBank4(mojePonudeBanci31.getId());
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://banka-3-dev.si.raf.edu.rs/exchange-service/api/v1/otcTrade/sendOffer"))
//                .header("Content-Type", "application/json")
//                .POST()
//                .build();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<?> response = restTemplate.exchange("https://banka-3-dev.si.raf.edu.rs/exchange-service/api/v1/otcTrade/sendOffer",
                HttpMethod.POST,new HttpEntity<>(ponudaBanci3Dto),String.class);
    }

    @Override
    public void potvrdiNasuPonudu(Long id) {
        // zove banka3 da prihvati nasu ponudu
        MojePonudeBanci3 mojePonudeBanci3 = mojePonudeBanci3Repository.findById(id).orElseThrow();
        ponudaRepository.banka3PrihvataPonudu(mojePonudeBanci3.getTicker(),Long.valueOf(mojePonudeBanci3.getQuantity()),new BigDecimal(mojePonudeBanci3.getAmountOffered()));
        mojePonudeBanci3.setStatus("PRIHVACENO");
        mojePonudeBanci3Repository.save(mojePonudeBanci3);
    }

    @Override
    public void odbijNasuPonudu(Long id) {
        MojePonudeBanci3 mojePonudeBanci3 = mojePonudeBanci3Repository.findById(id).orElseThrow();
        mojePonudeBanci3.setStatus("ODBIJENO");
        mojePonudeBanci3Repository.save(mojePonudeBanci3);
    }

    @Override
    public boolean prihvati(Long idPonude) {
        Ponuda ponuda = ponudaRepository.findById(idPonude).orElseThrow();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<?> response = restTemplate.exchange("https://banka-3-dev.si.raf.edu.rs/exchange-service/api/v1/otcTrade/offerAcepted/"+ponuda.getBanka3Id(),
                HttpMethod.POST,null,String.class);

        ponudaRepository.prihvatiPonudu(idPonude);
        return true;
    }

    @Override
    public void odbij(Long idPonude) {
        Ponuda ponuda = ponudaRepository.findById(idPonude).orElseThrow();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<?> response = restTemplate.exchange("https://banka-3-dev.si.raf.edu.rs/exchange-service/api/v1/otcTrade/offerDeclined/"+ponuda.getBanka3Id(),
                HttpMethod.POST,null,String.class);
        ponudaRepository.deleteById(idPonude);
    }

    @Override
    public List<PonudaDTO> svePonude() {
        return ponudaRepository.findAll().stream().map(ponudaMapper::ponudaToPonudaDto).toList();
    }
}
