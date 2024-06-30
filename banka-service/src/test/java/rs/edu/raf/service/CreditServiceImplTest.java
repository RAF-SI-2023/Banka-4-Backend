package rs.edu.raf.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.model.dto.CreditRequestDto;
import rs.edu.raf.model.dto.DetailedCreditDto;
import rs.edu.raf.model.entities.Credit;
import rs.edu.raf.model.entities.CreditRequest;
import rs.edu.raf.model.entities.racun.TekuciRacun;
import rs.edu.raf.model.mapper.CreditRequestMapper;
import rs.edu.raf.repository.CreditRepository;
import rs.edu.raf.repository.CreditRequestRepository;
import rs.edu.raf.service.racun.RacunServis;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class CreditServiceImplTest {

    @Mock
    private CreditRequestRepository creditRequestRepository;

    @Mock
    private CreditRequestMapper creditRequestMapper;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private RacunServis racunServis;

    @Spy
    @InjectMocks
    private CreditServiceImpl creditService;
//
//    @BeforeEach
//    void setUp() {
//        openMocks(this);
//    }

    @Test
    void applyForCredit() {
        CreditRequestDto creditRequestDto = new CreditRequestDto();
        CreditRequest creditRequest = new CreditRequest();

        given(creditRequestMapper.creditRequestDtoToCreditRequest(creditRequestDto)).willReturn(creditRequest);
        given(creditRequestRepository.save(any(CreditRequest.class))).willReturn(creditRequest);

        String result = creditService.applyForCredit(creditRequestDto);

        String expected = "Uspesno kreiran zahtev za kredit";

        assertEquals(expected, result);
        verify(creditRequestRepository, times(1)).save(any(CreditRequest.class));

    }

    @Test
    void approveCreditRequest() {
        Long id = 1L;

        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setId(id);
        creditRequest.setBankAccountNumber(123456789L);
        creditRequest.setAmount(BigDecimal.valueOf(1000));
        creditRequest.setType("housing");
        creditRequest.setLoanTerm(12L);
        creditRequest.setSalary(BigDecimal.valueOf(5000));

        TekuciRacun tekuciRacun = new TekuciRacun();
        tekuciRacun.setCurrency("RSD");

        given(creditRequestRepository.findById(id)).willReturn(Optional.of(creditRequest));
        given(racunServis.nadjiRacunPoBrojuRacuna(123456789L)).willReturn(tekuciRacun);
        doNothing().when(creditService).createCredit(any(CreditRequest.class), anyString(), anyString(), any(BigDecimal.class));

        String result = creditService.approveCreditRequest(id);

        String expected = "Prihvacen je zahtev za kredit";

        assertEquals(expected, result, "Expected message not returned");

        verify(creditRequestRepository, times(1)).findById(id);
        verify(creditService, times(1)).createCredit(any(CreditRequest.class), anyString(), anyString(), any(BigDecimal.class));

    }

    @Test
    void approveCreditRequest_NotFound() {
        Long id = 1L;

        given(creditRequestRepository.findById(id)).willReturn(Optional.empty());

        String result = creditService.approveCreditRequest(id);
        String expected = "Ne postoji takav zahtev za kredit";

        assertEquals(expected, result, "Expected message not returned");
        verify(creditRequestRepository, times(1)).findById(id);
        verify(creditService, times(0)).createCredit(any(CreditRequest.class), anyString(), anyString(), any(BigDecimal.class));
    }

    @Test
    void dennyCreditRequest() {
        Long id = 1L;
        CreditRequest creditRequest = new CreditRequest();

        given(creditRequestRepository.findById(id)).willReturn(Optional.of(creditRequest));

        String result = creditService.dennyCreditRequest(id);

        String expected = "Odbijen je zahtev za kredit";
        assertEquals(expected, result, "Expected message not returned");

        verify(creditRequestRepository, times(1)).findById(id);

    }

    @Test
    void dennyCreditRequest_NotFound() {
        Long id = 1L;

        given(creditRequestRepository.findById(id)).willReturn(Optional.empty());

        String result = creditService.dennyCreditRequest(id);
        String expected = "Ne postoji takav zahtev za kredit";

        assertEquals(expected, result, "Expected message not returned");
        verify(creditRequestRepository, times(1)).findById(id);
    }

    @Test
    void getAllCreditRequests() {
        String status = "approved";
        CreditRequest creditRequest = new CreditRequest();

        List<CreditRequest> creditRequests = List.of(creditRequest);
        CreditRequestDto creditRequestDto = new CreditRequestDto();

        given(creditRequestRepository.findAllByStatus(status)).willReturn(creditRequests);
        given(creditRequestMapper.creditRequestToCreditRequestDto(creditRequest)).willReturn(creditRequestDto);

        List<CreditRequestDto> result = creditService.getAllCreditRequests(status);

        assertNotNull(result, "Result should be not null");
        assertEquals(1, result.size());

        verify(creditRequestRepository, times(1)).findAllByStatus(status);
        verify(creditRequestMapper, times(1)).creditRequestToCreditRequestDto(creditRequest);

    }

    @Test
    void getAllCreditRequestForUser() {
        String status = "approved";
        CreditRequest creditRequest = new CreditRequest();

        List<CreditRequest> creditRequests = List.of(creditRequest);
        CreditRequestDto creditRequestDto = new CreditRequestDto();

        given(creditRequestRepository.findAllByStatus(status)).willReturn(creditRequests);
        given(creditRequestMapper.creditRequestToCreditRequestDto(creditRequest)).willReturn(creditRequestDto);

        List<CreditRequestDto> result = creditService.getAllCreditRequests(status);

        assertNotNull(result, "Result should be not null");
        assertEquals(1, result.size());

        verify(creditRequestRepository, times(1)).findAllByStatus(status);
        verify(creditRequestMapper, times(1)).creditRequestToCreditRequestDto(creditRequest);

    }

    @Test
    void createCredit() {
        CreditRequest creditRequest = new CreditRequest();

        creditRequest.setId(1L);
        creditRequest.setBankAccountNumber(123456789L);
        creditRequest.setAmount(BigDecimal.valueOf(1000));
        creditRequest.setType("housing");
        creditRequest.setLoanTerm(12L);
        creditRequest.setSalary(BigDecimal.valueOf(5000));

        creditService.createCredit(creditRequest, "tekuci","RSD", BigDecimal.valueOf(5000));

        verify(creditRepository, times(1)).save(any(Credit.class));

    }

    @Test
    void createCredit_WithoutAccountType() {
        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setId(1L);
        creditRequest.setBankAccountNumber(123456789L);
        creditRequest.setAmount(BigDecimal.valueOf(1000));
        creditRequest.setType("housing");
        creditRequest.setLoanTerm(12L);
        creditRequest.setSalary(BigDecimal.valueOf(5000));

        creditService.createCredit(creditRequest, null, "RSD", BigDecimal.valueOf(5000));
        verify(creditRepository, times(0)).save(any(Credit.class));
    }


    @Test
    void getDetailedCredit() {
        Long creditRequestId = 1L;
        Credit credit = new Credit();
        DetailedCreditDto detailedCreditDto = new DetailedCreditDto();

        given(creditRepository.findCreditByCreditRequestId(creditRequestId)).willReturn(credit);
        given(creditRequestMapper.creditToDetailedCreditDto(credit)).willReturn(detailedCreditDto);

        DetailedCreditDto result = creditService.getDetailedCredit(creditRequestId);

        assertNotNull(result, "Expecte non-null DetailedCreditDto");

        verify(creditRepository, times(1)).findCreditByCreditRequestId(creditRequestId);
        verify(creditRequestMapper, times(1)).creditToDetailedCreditDto(credit);

    }

    @Test
    void deleteCreditRequest() {
        Long creditRequestId = 1L;
        CreditRequest creditRequest = new CreditRequest();

        given(creditRequestRepository.findById(creditRequestId)).willReturn(Optional.of(creditRequest));
        doNothing().when(creditRequestRepository).delete(creditRequest);

        creditService.deleteCreditRequest(creditRequestId);

        verify(creditRequestRepository, times(1)).findById(creditRequestId);
        verify(creditRequestRepository, times(1)).delete(creditRequest);
    }

    @Test
    void deleteCreditRequest_NotFound() {
        Long creditRequestId = 1L;

        given(creditRequestRepository.findById(creditRequestId)).willReturn(Optional.empty());

        creditService.deleteCreditRequest(creditRequestId);

        verify(creditRequestRepository, times(1)).findById(creditRequestId);
        verify(creditRequestRepository, times(0)).delete(any(CreditRequest.class));
    }


}