package rs.edu.raf.mapper;

import rs.edu.raf.dto.SifrePlacanjaDTO;
import rs.edu.raf.model.SifrePlacanja;

public class SifrePlacanjaMapper{

    public static SifrePlacanjaDTO toDTO(SifrePlacanja source) {
        return SifrePlacanjaDTO.builder()
                .id(source.getId())
                .oblikIOsnov(source.getOblikIOsnov())
                .opisPlacanja(source.getOpisPlacanja())
                .build();
    }

    public static SifrePlacanja toEntity(SifrePlacanjaDTO source) {
        SifrePlacanja sifrePlacanja = new SifrePlacanja();
        sifrePlacanja.setId(source.getId());
        sifrePlacanja.setOblikIOsnov(source.getOblikIOsnov());
        sifrePlacanja.setOpisPlacanja(source.getOpisPlacanja());

        return sifrePlacanja;
    }

}
