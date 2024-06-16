package rs.edu.raf.order.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import rs.edu.raf.order.dto.DodajPonuduDto;
import rs.edu.raf.order.dto.PonudaDTO;
import rs.edu.raf.order.model.Ponuda;
import rs.edu.raf.order.repository.UserStockRepository;

@Component
@AllArgsConstructor
public class PonudaMapper {

    private UserStockRepository userStockRepository;

    public Ponuda ponudaDTOToPonuda(DodajPonuduDto ponudaDTO){
        Ponuda ponuda = new Ponuda();
        ponuda.setUserStock(userStockRepository.findByUserIdAndTicker(-1L,ponudaDTO.getTicker()));
        ponuda.setQuantity(ponudaDTO.getQuantity());
        ponuda.setAmountOffered(ponudaDTO.getAmountOffered());
        return ponuda;
    }

    public PonudaDTO ponudaToPonudaDto(Ponuda ponuda) {
        PonudaDTO ponudaDTO = new PonudaDTO();
        ponudaDTO.setId(ponuda.getId());
        ponudaDTO.setTicker(ponuda.getUserStock().getTicker());
        ponudaDTO.setQuantity(ponuda.getQuantity());
        ponudaDTO.setAmountOffered(ponuda.getAmountOffered());
        return ponudaDTO;
    }
}
