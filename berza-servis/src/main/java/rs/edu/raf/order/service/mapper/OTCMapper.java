package rs.edu.raf.order.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.edu.raf.order.dto.OTCDTO;
import rs.edu.raf.order.dto.OTCOfferDTO;
import rs.edu.raf.order.dto.OTCPlaceDTO;
import rs.edu.raf.order.model.OTC;
import rs.edu.raf.order.repository.UserStockRepository;

@Component
public class OTCMapper {
    @Autowired
    private UserStockRepository userStockRepository;

    public OTCDTO otcToOtcDto(OTC otc) {
        OTCDTO otcDto = new OTCDTO();

        otcDto.setId(otc.getId());
        otcDto.setTicker(userStockRepository.findById(otc.getStock_id()).orElseThrow().getTicker());
        otcDto.setSellerId(otc.getSellerId());
        otcDto.setBuyerId(otc.getBuyerId());
        otcDto.setSellerApproval(otc.getSellerApproval());
        otcDto.setBanksApproval(otc.getBanksApproval());
        otcDto.setQuantityToBuy(otc.getQuantityToBuy());
        otcDto.setPriceOffer(otc.getPriceOffer());
        otcDto.setRazlogOdbijanja(otc.getRazlogOdbijanja());
        otcDto.setDatumKreiranja(otc.getDatumKreiranja());
        otcDto.setDatumRealizacije(otc.getDatumRealizacije());
        otcDto.setOpis(otc.getOpis());

        return otcDto;
    }
}
