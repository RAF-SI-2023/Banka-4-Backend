package rs.edu.raf.order.service.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.order.dto.OTCDTO;
import rs.edu.raf.order.dto.OTCOfferDTO;
import rs.edu.raf.order.dto.OTCPlaceDTO;
import rs.edu.raf.order.model.OTC;

@Component
public class OTCMapper {

    public OTCDTO otcToOtcDto(OTC otc) {
        OTCDTO otcDTO = new OTCDTO();

        otcDTO.setOtcId(otc.getId());
        otcDTO.setTicker(otc.getTicker());
        otcDTO.setQuantity(otc.getQuantity());

        return otcDTO;
    }

    public OTC otcPlaceDTOToOtc(OTCPlaceDTO otcPlaceDTO) {
        OTC otc = new OTC();

        otc.setSellerId(otcPlaceDTO.getUserId());
        otc.setTicker(otcPlaceDTO.getTicker());
        otc.setQuantity(otcPlaceDTO.getQuantity());
        otc.setSellerApproval(false);
        otc.setBanksApproval(false);
        otc.setBuyerId(null);
        otc.setQuantityToBuy(null);
        otc.setPriceOffer(null);

        return otc;
    }

    public OTCOfferDTO otcToOtcOfferDto(OTC otc) {
        OTCOfferDTO otcOfferDTO = new OTCOfferDTO();

        otcOfferDTO.setOtcId(otc.getId());
        otcOfferDTO.setSellerId(otc.getSellerId());
        otcOfferDTO.setBuyerId(otc.getBuyerId());
        otcOfferDTO.setTicker(otc.getTicker());
        otcOfferDTO.setQuantity(otc.getQuantity());
        otcOfferDTO.setPriceOffered(otc.getPriceOffer());

        return otcOfferDTO;
    }
}
