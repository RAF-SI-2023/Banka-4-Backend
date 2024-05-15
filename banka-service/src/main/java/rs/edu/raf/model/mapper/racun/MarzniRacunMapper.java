package rs.edu.raf.model.mapper.racun;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.edu.raf.model.dto.racun.MarzniRacunCreateDTO;
import rs.edu.raf.model.dto.racun.MarzniRacunDTO;
import rs.edu.raf.model.entities.racun.MarzniRacun;
import rs.edu.raf.repository.transaction.MarzniRacunRepository;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class MarzniRacunMapper {

    public static MarzniRacun mapToEntity(MarzniRacunCreateDTO marzniRacunCreateDTO) {
        MarzniRacun marzniRacun = new MarzniRacun();
        marzniRacun.setVlasnik(marzniRacunCreateDTO.getVlasnik());
        marzniRacun.setBrojRacuna(marzniRacunCreateDTO.getBrojRacuna());
        marzniRacun.setValuta(marzniRacunCreateDTO.getValuta());
        marzniRacun.setGrupaHartija(marzniRacunCreateDTO.getGrupaHartija());
        marzniRacun.setUlozenaSredstva(BigDecimal.ZERO);
        marzniRacun.setLiquidCash(BigDecimal.ZERO);
        marzniRacun.setLoanValue(BigDecimal.ZERO);
        marzniRacun.setMaintenanceMargin(marzniRacunCreateDTO.getMaintenanceMargin());
        return marzniRacun;
    }

    public static MarzniRacunDTO mapToDTO(MarzniRacun marzniRacun) {
        MarzniRacunDTO marzniRacunDTO = new MarzniRacunDTO();
        marzniRacunDTO.setVlasnik(marzniRacun.getVlasnik());
        marzniRacunDTO.setBrojRacuna(marzniRacun.getBrojRacuna());
        marzniRacunDTO.setValuta(marzniRacun.getValuta());
        marzniRacunDTO.setGrupaHartija(marzniRacun.getGrupaHartija());
        marzniRacunDTO.setUlozenaSredstva(marzniRacun.getUlozenaSredstva());
        marzniRacunDTO.setLiquidCash(marzniRacun.getLiquidCash());
        marzniRacunDTO.setLoanValue(marzniRacun.getLoanValue());
        marzniRacunDTO.setMaintenanceMargin(marzniRacun.getMaintenanceMargin());
        marzniRacunDTO.setMarginCall(marzniRacun.getMarginCall());
        return marzniRacunDTO;
    }
}
