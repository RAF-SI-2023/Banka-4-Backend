package rs.edu.raf.order.service;

import rs.edu.raf.order.dto.Banka3StockDTO;
import rs.edu.raf.order.dto.DodajPonuduDto;
import rs.edu.raf.order.dto.PonudaBanci3Dto;
import rs.edu.raf.order.dto.PonudaDTO;
import rs.edu.raf.order.model.MojePonudeBanci3;
import rs.edu.raf.order.model.StranaPonudaDTO;
import rs.edu.raf.order.service.impl.PonudaServiceImpl;

import java.util.List;

public interface PonudaService {

    List<Banka3StockDTO> dohvatiStokoveBanke3();
    List<MojePonudeBanci3> svePonudeKaBanci3();

    void dodajPonudu(DodajPonuduDto ponudaDTO);
    void dodajPonuduBanci3(PonudaBanci3Dto ponudaBanci3Dto);

    void potvrdiNasuPonudu(Long id);
    void odbijNasuPonudu(Long id);
    boolean prihvati(Long idPonude);

    void odbij(Long idPonude);

    List<PonudaDTO> svePonude();
}
