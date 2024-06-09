package rs.edu.raf.order.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import rs.edu.raf.order.dto.Banka3StockDTO;
import rs.edu.raf.order.dto.DodajPonuduDto;
import rs.edu.raf.order.dto.PonudaDTO;
import rs.edu.raf.order.model.Ponuda;
import rs.edu.raf.order.model.StranaPonudaDTO;
import rs.edu.raf.order.repository.PonudaRepository;
import rs.edu.raf.order.service.PonudaService;
import rs.edu.raf.order.service.mapper.PonudaMapper;


import java.io.IOException;
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
    public void dodajPonudu(DodajPonuduDto ponudaDTO) {
        ponudaRepository.save(ponudaMapper.ponudaDTOToPonuda(ponudaDTO));
    }

    @Override
    public void potvrdiNasuPonudu(StranaPonudaDTO stranaPonudaDTO) {
        // zove banka3 da prihvati nasu ponudu
        ponudaRepository.banka3PrihvataPonudu(stranaPonudaDTO.getTicker(),Long.valueOf(stranaPonudaDTO.getQuantity()),stranaPonudaDTO.getAmountOffered());
    }

    @Override
    public boolean prihvati(Long idPonude) {

        ponudaRepository.prihvatiPonudu(idPonude);
        return true;
    }

    @Override
    public void odbij(Long idPonude) {
        ponudaRepository.deleteById(idPonude);
    }

    @Override
    public List<PonudaDTO> svePonude() {
        return ponudaRepository.findAll().stream().map(ponudaMapper::ponudaToPonudaDto).toList();
    }
}
