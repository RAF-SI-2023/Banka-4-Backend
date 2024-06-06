package rs.edu.raf.order.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import rs.edu.raf.order.dto.PonudaDTO;
import rs.edu.raf.order.model.Ponuda;
import rs.edu.raf.order.repository.UserStockRepository;

@Component
@AllArgsConstructor
public class PonudaMapper {

    private UserStockRepository userStockRepository;

    public Ponuda ponudaDTOToPonuda(PonudaDTO ponudaDTO){
        Ponuda ponuda = new Ponuda();
        ponuda.setUserStock(userStockRepository.findById(ponudaDTO.getStockID()).orElseThrow());
        ponuda.setQuantity(ponudaDTO.getQuantity());
        ponuda.setAmountOffered(ponudaDTO.getAmountOffered());
        return ponuda;
    }
}
