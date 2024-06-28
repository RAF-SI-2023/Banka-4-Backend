package rs.edu.raf.futures.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.annotations.GeneratedCrudOperation;
import rs.edu.raf.annotations.GeneratedOnlyIntegrationTestable;
import rs.edu.raf.annotations.GeneratedScheduledOperation;
import rs.edu.raf.futures.dto.FutureRequestDto;
import rs.edu.raf.futures.dto.FuturesContractDto;
import rs.edu.raf.futures.mapper.FuturesContractMapper;
import rs.edu.raf.futures.repository.FutureContractRequestRepository;
import rs.edu.raf.futures.repository.FuturesContractRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class FuturesServiceImpl implements FuturesService {
    private FuturesContractRepository futuresContractRepository;
    private FuturesContractMapper futuresContractMapper;
    private FutureContractRequestRepository futureContractRequestRepository;
    private TaskScheduler taskScheduler;

    @Override
    @GeneratedCrudOperation
    public List<FuturesContractDto> findByType(String type) {
        return futuresContractRepository.findAllByTypeAndKupacIdIsNull(type).stream().map(futuresContractMapper::futuresContractToFutureContractDto).toList();
    }

    @Override
    @GeneratedCrudOperation
    public List<FuturesContractDto> findByName(String name) {
        return futuresContractRepository.findAllByNameAndKupacIdIsNull(name).stream().map(futuresContractMapper::futuresContractToFutureContractDto).toList();
    }

    @Override
    @GeneratedCrudOperation
    public List<FuturesContractDto> findByKupac(Long kupacId) {
        return futuresContractRepository.findAllByKupacId(kupacId).stream().map(futuresContractMapper::futuresContractToFutureContractDto).toList();
    }

    @Override
    @GeneratedOnlyIntegrationTestable
    public String buy(Long id, Long kupacId, String racunId) {
//        FuturesContract f = futuresContractRepository.findById(id).orElseThrow();
//        if(f.getKupacId() != null) throw new RuntimeException("Vec je kupljeno!");
//
//        this.taskScheduler.schedule(() -> {
//            FuturesContract futuresContract = futuresContractRepository.findById(id).orElseThrow();
//            futuresContract.setBought(true);
//            futuresContractRepository.save(futuresContract);
//        }, Instant.ofEpochMilli(f.getSettlementDate()));
//        f.setKupacId(kupacId);

//        return futuresContractMapper.futuresContractToFutureContractDto(futuresContractRepository.save(f));
        try {
            futuresContractRepository.kupi_future_contract(kupacId,id,Long.valueOf(racunId));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
        return "Uspesna kupovina";
    }

    @Override
    @GeneratedCrudOperation
    public List<FutureRequestDto> allRequests(Long radnik_id) {
        return futureContractRequestRepository.allRequests(radnik_id).stream().map(futuresContractMapper::futureContractRequestToFutureContractRequestDto).toList();
    }

    @Override
    @GeneratedOnlyIntegrationTestable
    public void denyRequest(Long id) {
        futureContractRequestRepository.deny_request(id);
    }

    @Override
    @GeneratedOnlyIntegrationTestable
    public void approveRequest(Long id, Long supervisor_id) {
        futureContractRequestRepository.approve_request(id,supervisor_id);
    }
    @Scheduled(initialDelay = 240000, fixedRate = 120000)
    @GeneratedScheduledOperation
    public void confirmFutures() {
        try {
            futuresContractRepository.confirmFuture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
