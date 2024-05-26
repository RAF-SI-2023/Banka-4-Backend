package rs.edu.raf.model.mapper;


import org.springframework.stereotype.Component;
import rs.edu.raf.model.dto.CreditRequestDto;
import rs.edu.raf.model.dto.DetailedCreditDto;
import rs.edu.raf.model.entities.Credit;
import rs.edu.raf.model.entities.CreditRequest;

@Component
public class CreditRequestMapper {

    public CreditRequest creditRequestDtoToCreditRequest(CreditRequestDto creditRequestDto){

        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setAmount(creditRequestDto.getAmount());
        creditRequest.setBranchOffice(creditRequestDto.getBranchOffice());
        creditRequest.setSalary(creditRequestDto.getSalary());
        creditRequest.setType(creditRequestDto.getType());
        creditRequest.setLoanPurpose(creditRequestDto.getLoanPurpose());
        creditRequest.setBankAccountNumber(creditRequestDto.getBankAccountNumber());
        creditRequest.setLoanTerm(creditRequestDto.getLoanTerm());
        creditRequest.setPermanentEmployee(creditRequestDto.getPermanentEmployee());
        creditRequest.setCurrentEmploymentPeriod(creditRequestDto.getCurrentEmploymentPeriod());

        System.out.println("crdTOcr CreditRequestDto BAN :" + creditRequestDto.getBankAccountNumber());
        System.out.println("crdTOcr CreditRequest BAN: " + creditRequest.getBankAccountNumber());

        return creditRequest;
    }

    public CreditRequestDto creditRequestToCreditRequestDto(CreditRequest creditRequest){
        CreditRequestDto creditRequestDto = new CreditRequestDto();

        creditRequestDto.setId(creditRequest.getId());
        creditRequestDto.setAmount(creditRequest.getAmount());
        creditRequestDto.setBranchOffice(creditRequest.getBranchOffice());
        creditRequestDto.setSalary(creditRequest.getSalary());
        creditRequestDto.setType(creditRequest.getType());
        creditRequestDto.setLoanPurpose(creditRequest.getLoanPurpose());
        creditRequestDto.setBankAccountNumber(creditRequest.getBankAccountNumber());
        creditRequestDto.setLoanTerm(creditRequest.getLoanTerm());
        creditRequestDto.setPermanentEmployee(creditRequest.getPermanentEmployee());
        creditRequestDto.setCurrentEmploymentPeriod(creditRequest.getCurrentEmploymentPeriod());

        System.out.println("crTOcrd CreditRequestDto BAN :" + creditRequestDto.getBankAccountNumber());
        System.out.println("crTOcrd CreditRequest BAN: " + creditRequest.getBankAccountNumber());

        return creditRequestDto;
    }

    public DetailedCreditDto creditToDetailedCreditDto(Credit credit){
        DetailedCreditDto detailedCreditDto = new DetailedCreditDto();
        detailedCreditDto.setType(credit.getType());
        detailedCreditDto.setAmount(credit.getAmount());
        detailedCreditDto.setBankAccountNumber(credit.getBankAccountNumber());
        detailedCreditDto.setLoanTerm(credit.getLoanTerm());
        detailedCreditDto.setNominalInterestRate(credit.getNominalInterestRate());
        detailedCreditDto.setEffectiveInterestRate(credit.getEffectiveInterestRate());
        detailedCreditDto.setContractDate(credit.getContractDate());
        detailedCreditDto.setLoanMaturityDate(credit.getLoanMaturityDate());
        detailedCreditDto.setInstallmentAmount(credit.getInstallmentAmount());
        detailedCreditDto.setCurrency(credit.getCurrency());
        detailedCreditDto.setPrepayment(credit.getPrepayment());
        detailedCreditDto.setRemainingDebt(credit.getRemainingDebt());
        detailedCreditDto.setNextInstallmentDate(credit.getNextInstallmentDate());

        return detailedCreditDto;

    }

}
