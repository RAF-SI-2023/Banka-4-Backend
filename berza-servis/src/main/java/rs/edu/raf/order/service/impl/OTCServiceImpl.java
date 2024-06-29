package rs.edu.raf.order.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.order.dto.OTCDTO;
import rs.edu.raf.order.dto.OTCOfferDTO;
import rs.edu.raf.order.dto.OTCPlaceDTO;
import rs.edu.raf.order.dto.OTCResolveDTO;
import rs.edu.raf.order.exceptions.*;
import rs.edu.raf.order.model.OTC;
import rs.edu.raf.order.model.UserStock;
import rs.edu.raf.order.repository.OTCRepository;
import rs.edu.raf.order.repository.UserStockRepository;
import rs.edu.raf.order.service.OTCService;
import rs.edu.raf.order.service.mapper.OTCMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class OTCServiceImpl implements OTCService {

    private UserStockRepository userStockRepository;
    private OTCRepository otcRepository;
    private OTCMapper otcMapper;

    @Override
    public List<OTCDTO> getAllPublicOTC() {
        return otcRepository.findAll().stream().map(otcMapper::otcToOtcDto).toList();
    }

    @Override
    public List<OTCOfferDTO> getAllPendingOTC(Long id) {
        List<OTC> allOTC = otcRepository.findAll();
        List<OTCOfferDTO> pendingOTC = new ArrayList<>();

        for (OTC otc : allOTC){
            if (otc.getBuyerId() != null) {
                // An offer has to be made first (we know that because the buyer id is not null).
                if (otc.getSellerId().equals(id) && !otc.getSellerApproval()) {
                    // We're the seller and haven't confirmed the otc offer.
                    pendingOTC.add(otcMapper.otcToOtcOfferDto(otc));
                }
            }
        }

        return pendingOTC;
    }

    @Override
    public List<OTCOfferDTO> getAllPendingOTCForBank() {
        List<OTCOfferDTO> otcs = new ArrayList<>();

        for(OTC otc: otcRepository.findAll()) {
            if(otc.getBuyerId() != null && otc.getSellerId() != -1L && !otc.getBanksApproval()) {
                otcs.add(otcMapper.otcToOtcOfferDto(otc));
            }
        }
        return otcs;
    }

    @Override
    public void resolveOTC(OTCResolveDTO otcResolveDTO) {
        Optional<OTC> otc = otcRepository.findById(otcResolveDTO.getOtcId());

        if (otc.isEmpty()){
            throw new OTCOfferDoesntExist();
        }
        OTC otcOffer = otc.get();
        if (otcResolveDTO.getUserId() != -1L && !Objects.equals(otcOffer.getSellerId(), otcResolveDTO.getUserId())) {
            throw new YouDontOwnThisOTC();
        }

        // If declined, delete the otc offer(clear buyer info).
        if (!otcResolveDTO.isAccept()){
            otcOffer.setSellerApproval(false);
            otcOffer.setBanksApproval(false);
            otcOffer.setBuyerId(null);
            otcOffer.setQuantityToBuy(null);
            otcOffer.setPriceOffer(null);

            otcRepository.save(otcOffer);
            return;
        }

        if (otcResolveDTO.getUserId() == -1L){
            otcOffer.setBanksApproval(true);
        } if (otcResolveDTO.getUserId().equals(otcOffer.getSellerId())){
            otcOffer.setSellerApproval(true);
        }

        // Both seller and bank approved the otc offer, do the exchange of stocks and money.
        if (otcOffer.getBanksApproval() && otcOffer.getSellerApproval()) {
            // Subtract stock quantity(they have been already "reserved" from seller).
            otcRepository.deleteById(otcResolveDTO.getOtcId());

            // Add stock to buyer.
            UserStock stockToBeAddedToBuyer;
            stockToBeAddedToBuyer = userStockRepository.findByUserIdAndTicker(otcOffer.getBuyerId(), otcOffer.getTicker());
            if (stockToBeAddedToBuyer == null){
                stockToBeAddedToBuyer = new UserStock();
                stockToBeAddedToBuyer.setUserId(otcOffer.getBuyerId());
                stockToBeAddedToBuyer.setTicker(otcOffer.getTicker());
                stockToBeAddedToBuyer.setQuantity(otcOffer.getQuantityToBuy());
                stockToBeAddedToBuyer.setCurrentBid(new BigDecimal("1.0"));
                stockToBeAddedToBuyer.setCurrentAsk(new BigDecimal("1.0"));
            } else {
                stockToBeAddedToBuyer.setQuantity(stockToBeAddedToBuyer.getQuantity() + otcOffer.getQuantityToBuy());
            }
            userStockRepository.save(stockToBeAddedToBuyer);

            // Add money to seller and subtract from buyer

        }
    }

    @Override
    public void makeOfferForOTC(OTCOfferDTO otcOfferDTO) {
        OTC otcOffer = otcRepository.findOTCBySellerIdAndTicker(otcOfferDTO.getSellerId(), otcOfferDTO.getTicker());

        if (otcOffer == null) {
            throw new OTCOfferDoesntExist();
        } else if (otcOfferDTO.getQuantity() > otcOffer.getQuantity() || otcOfferDTO.getQuantity() <= 0) {
            throw new QuantityOfOTCDoesntExist();
        } else if (otcOfferDTO.getPriceOffered().compareTo(BigDecimal.ZERO) <= 0){
            throw new PriceMustBePositive();
        } else if (otcOffer.getBuyerId() != null) {
            throw new OTCOfferCurrentlyPlaced();
        }

        otcOffer.setBuyerId(otcOfferDTO.getBuyerId());
        otcOffer.setQuantityToBuy(otcOfferDTO.getQuantity());
        otcOffer.setPriceOffer(otcOfferDTO.getPriceOffered());

        // If we are the bank and want to buy this stock, approve the request right away.
        if (otcOfferDTO.getBuyerId() == -1L) {
            otcOffer.setBanksApproval(true);
        }

        otcRepository.save(otcOffer);
    }

    @Override
    public void placeOTCPublic(OTCPlaceDTO otcPlaceDTO) {
        UserStock userStock = userStockRepository.findByUserIdAndTicker(otcPlaceDTO.getUserId(), otcPlaceDTO.getTicker());

        if (userStock == null){
            throw new UserDoesntOwnTicker();
        } else if (otcPlaceDTO.getQuantity() > userStock.getQuantity() || otcPlaceDTO.getQuantity() <= 0){
            throw new UserDoesntOwnQuantityTicker();
        }

        // Subtract the amount so it can be put public to otc.
        userStock.setQuantity(userStock.getQuantity() - otcPlaceDTO.getQuantity());
        userStockRepository.save(userStock);

        OTC otcToAdd = otcMapper.otcPlaceDTOToOtc(otcPlaceDTO);

        // If we're the bank, and we're making our stocks public for buying approve the contract right away.
        if (otcPlaceDTO.getUserId() == -1L) {
            otcToAdd.setBanksApproval(true);
        }

        // Save the new public otc.
        otcRepository.save(otcToAdd);
    }
}
