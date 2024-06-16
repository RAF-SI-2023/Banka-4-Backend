package rs.edu.raf.futures.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import rs.edu.raf.futures.dto.FutureRequestDto;
import rs.edu.raf.futures.dto.FuturesContractDto;
import rs.edu.raf.futures.model.FutureContractRequest;
import rs.edu.raf.futures.model.FuturesContract;
import rs.edu.raf.futures.repository.FuturesContractRepository;

@Component
@AllArgsConstructor
public class FuturesContractMapper {

    private FuturesContractRepository futuresContractRepository;
    public FuturesContractDto futuresContractToFutureContractDto(FuturesContract futuresContract) {
        FuturesContractDto futuresContractDto = new FuturesContractDto();
        futuresContractDto.setId(futuresContract.getId());
        futuresContractDto.setContractSize(futuresContract.getContractSize());
        futuresContractDto.setContractUnit(futuresContract.getContractUnit());
        futuresContractDto.setPrice(futuresContract.getPrice());
        futuresContractDto.setType(futuresContract.getType());
        futuresContractDto.setName(futuresContract.getName());
        futuresContractDto.setMaintenanceMargin(futuresContract.getMaintenanceMargin());
        futuresContractDto.setSettlementDate(System.currentTimeMillis());
        return futuresContractDto;
    }

    public FutureRequestDto futureContractRequestToFutureContractRequestDto(FutureContractRequest futureContractRequest) {
        FutureRequestDto futureRequestDto = new FutureRequestDto();
        futureRequestDto.setId(futureContractRequest.getId());
        futureRequestDto.setEmail(futureContractRequest.getEmail());
        futureRequestDto.setIme(futureContractRequest.getIme());
        futureRequestDto.setPrezime(futureContractRequest.getPrezime());
        futureRequestDto.setBroj_telefona(futureContractRequest.getBroj_telefona());
        futureRequestDto.setRadnik_id(futureContractRequest.getRadnikId());
        futureRequestDto.setRacun_id(futureContractRequest.getRacunId());
        futureRequestDto.setFuturesContractDto(futuresContractToFutureContractDto(futuresContractRepository.findById(futureContractRequest.getId()).orElseThrow()));
        futureRequestDto.setStatus(futureContractRequest.getRequestStatus().toString());
        return futureRequestDto;
    }
}
