package rs.edu.raf.racun.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rs.edu.raf.model.dto.racun.MarzniRacunCreateDTO;
import rs.edu.raf.model.dto.racun.MarzniRacunDTO;
import rs.edu.raf.model.entities.racun.MarzniRacun;
import rs.edu.raf.model.mapper.racun.MarzniRacunMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MarzniRacunMapperTest {

    private MarzniRacunCreateDTO marzniRacunCreateDTO;

    private MarzniRacun marzniRacun;


    @BeforeEach
    void setUp() {
        marzniRacunCreateDTO = new MarzniRacunCreateDTO();
        marzniRacunCreateDTO.setVlasnik(1L);
        marzniRacunCreateDTO.setBrojRacuna(123456L);
        marzniRacunCreateDTO.setValuta("RSD");
        marzniRacunCreateDTO.setGrupaHartija("Grupa");
        marzniRacunCreateDTO.setMaintenanceMargin(BigDecimal.valueOf(1000));

        marzniRacun = new MarzniRacun();
        marzniRacun.setVlasnik(1L);
        marzniRacun.setBrojRacuna(123456L);
        marzniRacun.setValuta("RSD");
        marzniRacun.setGrupaHartija("Grupa");
        marzniRacun.setUlozenaSredstva(BigDecimal.ZERO);
        marzniRacun.setLiquidCash(BigDecimal.ZERO);
        marzniRacun.setLoanValue(BigDecimal.ZERO);
        marzniRacun.setMaintenanceMargin(BigDecimal.valueOf(1000));
        marzniRacun.setMarginCall(false);
    }

    @Test
    @DisplayName("mapToEntity should map DTO to entity correctly")
    void mapToEntityShouldMapDtoToEntityCorrectly() {
        MarzniRacun marzniRacun = MarzniRacunMapper.mapToEntity(marzniRacunCreateDTO);

        assertEquals(marzniRacunCreateDTO.getVlasnik(), marzniRacun.getVlasnik());
        assertEquals(marzniRacunCreateDTO.getBrojRacuna(), marzniRacun.getBrojRacuna());
        assertEquals(marzniRacunCreateDTO.getValuta(), marzniRacun.getValuta());
        assertEquals(marzniRacunCreateDTO.getGrupaHartija(), marzniRacun.getGrupaHartija());
        assertEquals(BigDecimal.ZERO, marzniRacun.getUlozenaSredstva());
        assertEquals(BigDecimal.ZERO, marzniRacun.getLiquidCash());
        assertEquals(BigDecimal.ZERO, marzniRacun.getLoanValue());
        assertEquals(marzniRacunCreateDTO.getMaintenanceMargin(), marzniRacun.getMaintenanceMargin());
        assertFalse(marzniRacun.getMarginCall());
    }

    @Test
    @DisplayName("mapToDTO should map entity to DTO correctly")
    void mapToDTOShouldMapEntityToDtoCorrectly() {
        MarzniRacunDTO marzniRacunDTO = MarzniRacunMapper.mapToDTO(marzniRacun);

        assertEquals(marzniRacun.getVlasnik(), marzniRacunDTO.getVlasnik());
        assertEquals(marzniRacun.getBrojRacuna(), marzniRacunDTO.getBrojRacuna());
        assertEquals(marzniRacun.getValuta(), marzniRacunDTO.getValuta());
        assertEquals(marzniRacun.getGrupaHartija(), marzniRacunDTO.getGrupaHartija());
        assertEquals(marzniRacun.getUlozenaSredstva(), marzniRacunDTO.getUlozenaSredstva());
        assertEquals(marzniRacun.getLiquidCash(), marzniRacunDTO.getLiquidCash());
        assertEquals(marzniRacun.getLoanValue(), marzniRacunDTO.getLoanValue());
        assertEquals(marzniRacun.getMaintenanceMargin(), marzniRacunDTO.getMaintenanceMargin());
        assertEquals(marzniRacun.getMarginCall(), marzniRacunDTO.getMarginCall());
    }
}