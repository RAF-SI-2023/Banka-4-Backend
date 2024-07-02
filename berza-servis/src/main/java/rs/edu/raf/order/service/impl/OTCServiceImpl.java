package rs.edu.raf.order.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.annotations.GeneratedLegacyCode;
import rs.edu.raf.order.dto.*;
import rs.edu.raf.order.exceptions.*;
import rs.edu.raf.order.model.OTC;
import rs.edu.raf.order.model.UserStock;
import rs.edu.raf.order.repository.OTCRepository;
import rs.edu.raf.order.repository.UserStockRepository;
import rs.edu.raf.order.service.OTCService;
import rs.edu.raf.order.service.mapper.OTCMapper;
import rs.edu.raf.order.service.mapper.PublicUserStockMapper;

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
    private PublicUserStockMapper publicUserStockMapper;

    @Override
    public List<PublicUserStockDto> getAllPublicOTC(Long id) {
//        return otcRepository.findAll().stream().map(otcMapper::otcToOtcDto).toList();
        List<PublicUserStockDto> publicUserStockDtos = new ArrayList<>();
        for(UserStock userStock: userStockRepository.findAll()) {
            if(userStock.getUserId() * id > 0 && userStock.getPublicQuantity() > 0 && userStock.getUserId() != id)
                publicUserStockDtos.add(publicUserStockMapper.userStockToPublicUserStockDto(userStock));
        }
        return publicUserStockDtos;
    }

    @Override
    public List<OTCDTO> getAllPendingOTC(Long id) {
        List<OTC> allOTC = otcRepository.findAll();
        List<OTCDTO> pendingOTC = new ArrayList<>();

        for (OTC otc : allOTC){
            if (otc.getBuyerId() != null) {
                // An offer has to be made first (we know that because the buyer id is not null).
                if (otc.getSellerId().equals(id) && !otc.isResen()) {
                    // We're the seller and haven't confirmed the otc offer.
                    pendingOTC.add(otcMapper.otcToOtcDto(otc));
                }
            }
        }

        return pendingOTC;
    }

    @Override
    public List<OTCDTO> getAllPendingOTCForBank() {
        List<OTCDTO> otcs = new ArrayList<>();

        for(OTC otc: otcRepository.findAll()) {
            if(otc.getSellerId() != -1L && !otc.isResen()) {
                otcs.add(otcMapper.otcToOtcDto(otc));
            }
        }
        return otcs;
    }

    @Override
    public List<OTCDTO> getAllCompletedOTC(Long id) {
        List<OTCDTO> otcs = new ArrayList<>();


        for(OTC otc: otcRepository.findAll()) {
            if((otc.getSellerId() == id || otc.getBuyerId() == id) && otc.isResen()) {
                otcs.add(otcMapper.otcToOtcDto(otc));
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
//            otcOffer.setSellerApproval(false);
//            otcOffer.setBanksApproval(false);
//            otcOffer.setBuyerId(null);
//            otcOffer.setQuantityToBuy(null);
//            otcOffer.setPriceOffer(null);
//
//            otcRepository.save(otcOffer);
//            return;
            if(otcResolveDTO.getUserId() == -1L)
                otcOffer.setBanksApproval(false);
            otcOffer.setSellerApproval(false);
            otcOffer.setRazlogOdbijanja(otcResolveDTO.getRazlog());
            otcOffer.setResen(true);
            otcRepository.save(otcOffer);
            return;
        }

        if (otcResolveDTO.getUserId() == -1L){
            otcOffer.setBanksApproval(true);
        } if (otcResolveDTO.getUserId().equals(otcOffer.getSellerId())){
            otcOffer.setSellerApproval(true);
            otcOffer.setOpis(otcResolveDTO.getOpis());
        }

        // Both seller and bank approved the otc offer, do the exchange of stocks and money.
        if (otcOffer.getBanksApproval() && otcOffer.getSellerApproval()) {
            // Subtract stock quantity(they have been already "reserved" from seller).
//            otcRepository.deleteById(otcResolveDTO.getOtcId());
            otcOffer.setDatumRealizacije(System.currentTimeMillis());
            otcOffer.setOpis(otcResolveDTO.getOpis());
            otcOffer.setResen(true);

            UserStock soldStock = userStockRepository.findById(otcOffer.getStock_id()).orElseThrow();
            // Add stock to buyer.
            UserStock stockToBeAddedToBuyer;
            stockToBeAddedToBuyer = userStockRepository.findByUserIdAndTicker(otcOffer.getBuyerId(), soldStock.getTicker());

            if (stockToBeAddedToBuyer == null){
                stockToBeAddedToBuyer = new UserStock();
                stockToBeAddedToBuyer.setUserId(otcOffer.getBuyerId());
                stockToBeAddedToBuyer.setTicker(soldStock.getTicker());
                stockToBeAddedToBuyer.setQuantity(otcOffer.getQuantityToBuy());
                stockToBeAddedToBuyer.setCurrentBid(new BigDecimal("1.0"));
                stockToBeAddedToBuyer.setCurrentAsk(new BigDecimal("1.0"));
            } else {
                stockToBeAddedToBuyer.setQuantity(stockToBeAddedToBuyer.getQuantity() + otcOffer.getQuantityToBuy());
            }
            userStockRepository.save(stockToBeAddedToBuyer);

            soldStock.setQuantity(soldStock.getQuantity() - otcOffer.getQuantityToBuy());
            soldStock.setPublicQuantity(soldStock.getPublicQuantity() - otcOffer.getQuantityToBuy());
            userStockRepository.save(soldStock);

            // Add money to seller and subtract from buyer
            // TODO: prebacivanje para
            otcRepository.prebaciNovac(otcOffer.getBuyerId(),otcOffer.getSellerId(),otcOffer.getPriceOffer());
        }
        else otcRepository.save(otcOffer);
    }

    @Override
    public void makeOfferForOTC(OTCOfferDTO otcOfferDTO) {
        UserStock otcOffer = userStockRepository.findById(otcOfferDTO.getStockId()).orElseThrow();

        if (otcOffer == null) {
            throw new OTCOfferDoesntExist();
        } else if (otcOfferDTO.getQuantity() > otcOffer.getPublicQuantity() || otcOfferDTO.getQuantity() <= 0) {
            throw new QuantityOfOTCDoesntExist();
        } else if (otcOfferDTO.getPriceOffered().compareTo(BigDecimal.ZERO) <= 0){
            throw new PriceMustBePositive();
        } else if(otcOfferDTO.getBuyerId() * otcOffer.getUserId() < 0)
            throw new RuntimeException("You can't place order!");

        OTC otc = new OTC();
        otc.setStock_id(otcOffer.getId());
        otc.setSellerId(otcOffer.getUserId());
        otc.setQuantityToBuy(otcOfferDTO.getQuantity());
        otc.setPriceOffer(otcOfferDTO.getPriceOffered());
        otc.setBuyerId(otcOfferDTO.getBuyerId());
        otc.setDatumKreiranja(System.currentTimeMillis());
        otc.setBanksApproval(false);
        otc.setSellerApproval(false);
        otc.setResen(false);

//        otcOffer.setBuyerId(otcOfferDTO.getBuyerId());
//        otcOffer.setQuantityToBuy(otcOfferDTO.getQuantity());
//        otcOffer.setPriceOffer(otcOfferDTO.getPriceOffered());

        // If we are the bank and want to buy this stock, approve the request right away.
//        if (otcOfferDTO.getBuyerId() == -1L) {
//            otcOffer.setBanksApproval(true);
//        }
        if (otcOfferDTO.getBuyerId() == -1L) {
            otc.setBanksApproval(true);
        }

        otcRepository.save(otc);
    }

    @Override
    @GeneratedLegacyCode
    public void placeOTCPublic(OTCPlaceDTO otcPlaceDTO, Long userId) {
//        UserStock userStock = userStockRepository.findByUserIdAndTicker(userId, otcPlaceDTO.getTicker());
//
//        if (userStock == null){
//            throw new UserDoesntOwnTicker();
//        } else if (otcPlaceDTO.getQuantity() > userStock.getQuantity() || otcPlaceDTO.getQuantity() <= 0){
//            throw new UserDoesntOwnQuantityTicker();
//        }
//
//        // Subtract the amount so it can be put public to otc.
////        userStock.setQuantity(userStock.getQuantity() - otcPlaceDTO.getQuantity());
////        userStockRepository.save(userStock);
//
//        OTC otcToAdd = otcMapper.otcPlaceDTOToOtc(otcPlaceDTO, userId, userStock.getId());
//
//        // If we're the bank, and we're making our stocks public for buying approve the contract right away.
//        if (userId == -1L) {
//            otcToAdd.setBanksApproval(true);
//        }
//
//        // Save the new public otc.
//        otcRepository.save(otcToAdd);
    }
}
