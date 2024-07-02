package rs.edu.raf.otc_unutar_banke;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import rs.edu.raf.order.dto.OTCDTO;
import rs.edu.raf.order.dto.OTCOfferDTO;
import rs.edu.raf.order.dto.OTCResolveDTO;
import rs.edu.raf.order.dto.PublicUserStockDto;
import rs.edu.raf.order.exceptions.OTCOfferDoesntExist;
import rs.edu.raf.order.exceptions.PriceMustBePositive;
import rs.edu.raf.order.exceptions.QuantityOfOTCDoesntExist;
import rs.edu.raf.order.exceptions.YouDontOwnThisOTC;
import rs.edu.raf.order.model.OTC;
import rs.edu.raf.order.model.UserStock;
import rs.edu.raf.order.repository.OTCRepository;
import rs.edu.raf.order.repository.UserStockRepository;
import rs.edu.raf.order.service.impl.OTCServiceImpl;
import rs.edu.raf.order.service.mapper.OTCMapper;
import rs.edu.raf.order.service.mapper.PublicUserStockMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public class OTCUnitTest {
    @Mock
    private UserStockRepository userStockRepository;
    @Mock
    private OTCRepository otcRepository;
    @Mock
    private OTCMapper otcMapper;
    @Mock
    private PublicUserStockMapper publicUserStockMapper;
    @InjectMocks
    private OTCServiceImpl otcService;

    @Test
    public void testgetAllPublicOtcKorisnikFirma() {
        UserStock userStock = new UserStock();
        userStock.setUserId(-1L);
        when(userStockRepository.findAll()).thenReturn(List.of(userStock));

        List<PublicUserStockDto> result = otcService.getAllPublicOTC(1L);
        assertEquals(0,result.size());
    }

    @Test
    public void testgetAllPublicOtcNemaPublic() {
        UserStock userStock = new UserStock();
        userStock.setUserId(-1L);
        userStock.setPublicQuantity(0);
        when(userStockRepository.findAll()).thenReturn(List.of(userStock));

        List<PublicUserStockDto> result = otcService.getAllPublicOTC(-2L);
        assertEquals(0,result.size());
    }

    @Test
    public void testgetAllPublicOtcZaSebe() {
        UserStock userStock = new UserStock();
        userStock.setUserId(-1L);
        userStock.setPublicQuantity(1);
        when(userStockRepository.findAll()).thenReturn(List.of(userStock));

        List<PublicUserStockDto> result = otcService.getAllPublicOTC(-1L);
        assertEquals(0,result.size());
    }

    @Test
    public void testgetAllPublicOtc() {
        UserStock userStock = new UserStock();
        userStock.setUserId(-1L);
        userStock.setPublicQuantity(1);
        when(userStockRepository.findAll()).thenReturn(List.of(userStock));

        List<PublicUserStockDto> result = otcService.getAllPublicOTC(-2L);
        assertEquals(1,result.size());
    }

    @Test
    public void testgetAllPendingOtc() {
        when(otcRepository.findAll()).thenReturn(List.of());
        List<OTCDTO> result = otcService.getAllPendingOTC(-2L);
        assertEquals(0,result.size());

        when(otcRepository.findAll()).thenReturn(List.of(new OTC()));
        result = otcService.getAllPendingOTC(-2L);
        assertEquals(0,result.size());

        OTC otc = new OTC();
        otc.setBuyerId(-1L);
        otc.setSellerId(-3L);
        when(otcRepository.findAll()).thenReturn(List.of(otc));
        result = otcService.getAllPendingOTC(-2L);
        assertEquals(0,result.size());

        otc.setSellerId(-2L);
        otc.setResen(true);
        when(otcRepository.findAll()).thenReturn(List.of(otc));
        result = otcService.getAllPendingOTC(-2L);
        assertEquals(0,result.size());

        otc.setResen(false);
        when(otcRepository.findAll()).thenReturn(List.of(otc));
        result = otcService.getAllPendingOTC(-2L);
        assertEquals(1,result.size());

    }

    @Test
    public void testgetAllPendingOtcForBank() {
        when(otcRepository.findAll()).thenReturn(List.of());
        List<OTCDTO> result = otcService.getAllPendingOTCForBank();
        assertEquals(0,result.size());

        OTC otc = new OTC();
        otc.setSellerId(-1L);
        when(otcRepository.findAll()).thenReturn(List.of(otc));
        result = otcService.getAllPendingOTCForBank();
        assertEquals(0,result.size());

        otc.setSellerId(-2L);
        otc.setResen(true);
        when(otcRepository.findAll()).thenReturn(List.of(otc));
        result = otcService.getAllPendingOTCForBank();
        assertEquals(0,result.size());

        otc.setResen(false);
        when(otcRepository.findAll()).thenReturn(List.of(otc));
        result = otcService.getAllPendingOTCForBank();
        assertEquals(1,result.size());

    }

    @Test
    public void testgetAllCompletedOTC() {
        when(otcRepository.findAll()).thenReturn(List.of());
        List<OTCDTO> result = otcService.getAllCompletedOTC(-1L);
        assertEquals(0,result.size());

        OTC otc = new OTC();
        otc.setSellerId(-2L);
        otc.setBuyerId(-3L);
        when(otcRepository.findAll()).thenReturn(List.of(otc));
        result = otcService.getAllCompletedOTC(-1L);
        assertEquals(0,result.size());

        otc.setSellerId(-1L);
        otc.setResen(false);
        when(otcRepository.findAll()).thenReturn(List.of(otc));
        result = otcService.getAllCompletedOTC(-1L);
        assertEquals(0,result.size());

        otc.setSellerId(-2L);
        otc.setBuyerId(-1L);
        otc.setResen(false);
        when(otcRepository.findAll()).thenReturn(List.of(otc));
        result = otcService.getAllCompletedOTC(-1L);
        assertEquals(0,result.size());

        otc.setSellerId(-1L);
        otc.setBuyerId(-1L);
        otc.setResen(false);
        when(otcRepository.findAll()).thenReturn(List.of(otc));
        result = otcService.getAllCompletedOTC(-1L);
        assertEquals(0,result.size());

        otc.setSellerId(-1L);
        otc.setBuyerId(-1L);
        otc.setResen(true);
        when(otcRepository.findAll()).thenReturn(List.of(otc));
        result = otcService.getAllCompletedOTC(-1L);
        assertEquals(1,result.size());

        otc.setSellerId(-2L);
        otc.setBuyerId(-1L);
        otc.setResen(true);
        when(otcRepository.findAll()).thenReturn(List.of(otc));
        result = otcService.getAllCompletedOTC(-1L);
        assertEquals(1,result.size());

        otc.setSellerId(-1L);
        otc.setBuyerId(-2L);
        otc.setResen(true);
        when(otcRepository.findAll()).thenReturn(List.of(otc));
        result = otcService.getAllCompletedOTC(-1L);
        assertEquals(1,result.size());

        otc.setSellerId(-2L);
        otc.setBuyerId(-3L);
        otc.setResen(true);
        when(otcRepository.findAll()).thenReturn(List.of(otc));
        result = otcService.getAllCompletedOTC(-1L);
        assertEquals(0,result.size());
    }

    @Test
    public void testResolveOTCNeValjaNista() {
        OTCResolveDTO otcResolveDTO = new OTCResolveDTO();
        otcResolveDTO.setUserId(-2L);
        otcResolveDTO.setOtcId(1L);
        when(otcRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(OTCOfferDoesntExist.class,()->otcService.resolveOTC(otcResolveDTO));

        OTC otc = new OTC();
        otc.setSellerId(-1L);
        otc.setBuyerId(-2L);
        when(otcRepository.findById(anyLong())).thenReturn(Optional.of(otc));
        assertThrows(YouDontOwnThisOTC.class,()->otcService.resolveOTC(otcResolveDTO));
    }

    @Test
    public void testResolveOTCOdbijanjeBankaVlasnik() {
        OTCResolveDTO otcResolveDTO = new OTCResolveDTO();
        otcResolveDTO.setUserId(-1L);
        otcResolveDTO.setOtcId(1L);
        otcResolveDTO.setAccept(false);
        OTC otc = new OTC();
        otc.setSellerId(-1L);
        otc.setBuyerId(-2L);
        OTC spy = spy(otc);
        Optional<OTC> optionalOTC = Optional.of(spy);
        when(otcRepository.findById(anyLong())).thenReturn(optionalOTC);
        when(otcRepository.save(optionalOTC.get())).thenReturn(otc);
        otcService.resolveOTC(otcResolveDTO);
        verify(optionalOTC.get()).setBanksApproval(false);
        verify(optionalOTC.get()).setSellerApproval(false);
    }

    @Test
    public void testResolveOTCOdbijanjeObicanVlasnik() {
        OTCResolveDTO otcResolveDTO = new OTCResolveDTO();
        otcResolveDTO.setUserId(-2L);
        otcResolveDTO.setOtcId(1L);
        otcResolveDTO.setAccept(false);
        OTC otc = new OTC();
        otc.setSellerId(-2L);
        otc.setBuyerId(-2L);
        OTC spy = spy(otc);
        Optional<OTC> optionalOTC = Optional.of(spy);
        when(otcRepository.findById(anyLong())).thenReturn(optionalOTC);
        when(otcRepository.save(optionalOTC.get())).thenReturn(otc);
        otcService.resolveOTC(otcResolveDTO);
        verify(optionalOTC.get(),never()).setBanksApproval(false);
        verify(optionalOTC.get()).setSellerApproval(false);
    }

    @Test
    public void testResolveOTCPrihvatanjeObicanVlasnikBankaNijePrihvatila() {
        OTCResolveDTO otcResolveDTO = new OTCResolveDTO();
        otcResolveDTO.setUserId(-2L);
        otcResolveDTO.setOtcId(1L);
        otcResolveDTO.setAccept(true);
        OTC otc = new OTC();
        otc.setSellerId(-2L);
        otc.setBuyerId(-2L);
        otc.setBanksApproval(false);
        OTC spy = spy(otc);
        Optional<OTC> optionalOTC = Optional.of(spy);
        when(otcRepository.findById(anyLong())).thenReturn(optionalOTC);
        when(otcRepository.save(optionalOTC.get())).thenReturn(otc);
        otcService.resolveOTC(otcResolveDTO);
        verify(optionalOTC.get(),never()).setBanksApproval(true);
        verify(optionalOTC.get()).setSellerApproval(true);
        verify(otcRepository).save(any());
    }

    @Test
    public void testResolveOTCPrihvatanjeObicanVlasnikBankaPrihvatilaNemaStock() {
        OTCResolveDTO otcResolveDTO = new OTCResolveDTO();
        otcResolveDTO.setUserId(-2L);
        otcResolveDTO.setOtcId(1L);
        otcResolveDTO.setAccept(true);
        OTC otc = new OTC();
        otc.setSellerId(-2L);
        otc.setBuyerId(-2L);
        otc.setBanksApproval(true);
        otc.setSellerApproval(true);
        otc.setStock_id(1L);
        otc.setQuantityToBuy(5);
        OTC spy = spy(otc);
        Optional<OTC> optionalOTC = Optional.of(spy);
        UserStock stock = new UserStock();
        stock.setUserId(-2L);
        stock.setTicker("AAPL");
        stock.setQuantity(55);
        when(userStockRepository.save(any())).thenReturn(stock);
        when(userStockRepository.findByUserIdAndTicker(anyLong(),anyString())).thenReturn(null);
        when(userStockRepository.findById(anyLong())).thenReturn(Optional.of(stock));
        when(otcRepository.findById(anyLong())).thenReturn(optionalOTC);
//        when(otcRepository.save(optionalOTC.get())).thenReturn(otc);
        otcService.resolveOTC(otcResolveDTO);
        verify(optionalOTC.get(),never()).setBanksApproval(true);
        verify(optionalOTC.get()).setSellerApproval(true);
        verify(otcRepository,never()).save(any());
    }

    @Test
    public void testResolveOTCPrihvatanjeObicanVlasnikBankaPrihvatilaImaStock() {
        OTCResolveDTO otcResolveDTO = new OTCResolveDTO();
        otcResolveDTO.setUserId(-2L);
        otcResolveDTO.setOtcId(1L);
        otcResolveDTO.setAccept(true);
        OTC otc = new OTC();
        otc.setSellerId(-2L);
        otc.setBuyerId(-2L);
        otc.setBanksApproval(true);
        otc.setSellerApproval(true);
        otc.setStock_id(1L);
        otc.setQuantityToBuy(5);
        OTC spy = spy(otc);
        Optional<OTC> optionalOTC = Optional.of(spy);
        UserStock stock = new UserStock();
        stock.setUserId(-2L);
        stock.setTicker("AAPL");
        stock.setQuantity(55);
        when(userStockRepository.save(any())).thenReturn(stock);
        when(userStockRepository.findByUserIdAndTicker(anyLong(),anyString())).thenReturn(stock);
        when(userStockRepository.findById(anyLong())).thenReturn(Optional.of(stock));
        when(otcRepository.findById(anyLong())).thenReturn(optionalOTC);
//        when(otcRepository.save(optionalOTC.get())).thenReturn(otc);
        otcService.resolveOTC(otcResolveDTO);
        verify(optionalOTC.get(),never()).setBanksApproval(true);
        verify(optionalOTC.get()).setSellerApproval(true);
        verify(otcRepository,never()).save(any());
    }

    @Test
    public void testResolveOTCPrihvatanjeBankaPrihvatilaImaStock() {
        OTCResolveDTO otcResolveDTO = new OTCResolveDTO();
        otcResolveDTO.setUserId(-1L);
        otcResolveDTO.setOtcId(1L);
        otcResolveDTO.setAccept(true);
        OTC otc = new OTC();
        otc.setSellerId(-1L);
        otc.setBuyerId(-2L);
        otc.setBanksApproval(true);
        otc.setSellerApproval(true);
        otc.setStock_id(1L);
        otc.setQuantityToBuy(5);
        OTC spy = spy(otc);
        Optional<OTC> optionalOTC = Optional.of(spy);
        UserStock stock = new UserStock();
        stock.setUserId(-2L);
        stock.setTicker("AAPL");
        stock.setQuantity(55);
        when(userStockRepository.save(any())).thenReturn(stock);
        when(userStockRepository.findByUserIdAndTicker(anyLong(),anyString())).thenReturn(stock);
        when(userStockRepository.findById(anyLong())).thenReturn(Optional.of(stock));
        when(otcRepository.findById(anyLong())).thenReturn(optionalOTC);
//        when(otcRepository.save(optionalOTC.get())).thenReturn(otc);
        otcService.resolveOTC(otcResolveDTO);
        verify(optionalOTC.get()).setBanksApproval(true);
        verify(optionalOTC.get()).setSellerApproval(true);
        verify(otcRepository,never()).save(any());
    }


    @Test
    public void testMakeOffer() {
        OTCOfferDTO otcOfferDTO = new OTCOfferDTO();
        otcOfferDTO.setQuantity(0);
        otcOfferDTO.setStockId(1L);
//        when(userStockRepository.findById(anyLong())).thenReturn(null);
//        assertThrows(OTCOfferDoesntExist.class,()->otcService.makeOfferForOTC(otcOfferDTO));

        UserStock userStock = new UserStock();
        userStock.setPublicQuantity(3);
        when(userStockRepository.findById(anyLong())).thenReturn(Optional.of(userStock));
        assertThrows(QuantityOfOTCDoesntExist.class, ()->otcService.makeOfferForOTC(otcOfferDTO));

        otcOfferDTO.setQuantity(5);
        assertThrows(QuantityOfOTCDoesntExist.class, ()->otcService.makeOfferForOTC(otcOfferDTO));

        otcOfferDTO.setPriceOffered(BigDecimal.ZERO);
        otcOfferDTO.setQuantity(3);
        assertThrows(PriceMustBePositive.class, ()->otcService.makeOfferForOTC(otcOfferDTO));

        otcOfferDTO.setBuyerId(-1L);
        userStock.setUserId(2L);
        otcOfferDTO.setPriceOffered(BigDecimal.TEN);
        assertThrows(RuntimeException.class, ()->otcService.makeOfferForOTC(otcOfferDTO));

        otcOfferDTO.setBuyerId(1L);
        OTC otc = new OTC();
        OTC spy = spy(otc);
        when(otcRepository.save(any())).thenReturn(spy);
        otcService.makeOfferForOTC(otcOfferDTO);
        verify(spy,never()).setBanksApproval(true);

        otcOfferDTO.setBuyerId(-1L);
        userStock.setUserId(-1L);
        when(otcRepository.save(any())).thenReturn(spy);
        otcService.makeOfferForOTC(otcOfferDTO);
    }
}
