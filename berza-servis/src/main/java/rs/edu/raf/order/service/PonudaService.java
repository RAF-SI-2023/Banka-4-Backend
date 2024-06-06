package rs.edu.raf.order.service;

import rs.edu.raf.order.dto.Banka3StockDTO;
import rs.edu.raf.order.dto.PonudaDTO;
import rs.edu.raf.order.model.StranaPonudaDTO;

import java.util.List;

public interface PonudaService {

    List<Banka3StockDTO> dohvatiStokoveBanke3();

    void dodajPonudu(PonudaDTO ponudaDTO);

    void potvrdiNasuPonudu(StranaPonudaDTO stranaPonudaDTO);

    boolean prihvati(Long idPonude);

    void odbij(Long idPonude);
}
