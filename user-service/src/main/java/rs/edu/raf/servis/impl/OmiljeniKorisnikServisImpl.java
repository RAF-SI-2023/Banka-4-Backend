package rs.edu.raf.servis.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.edu.raf.dto.OmiljeniKorisnikDTO;
import rs.edu.raf.exceptions.UserNotFoundException;
import rs.edu.raf.repository.OmiljeniKorisnikRepository;
import rs.edu.raf.mapper.OmiljeniKorisnikMapper;
import rs.edu.raf.servis.OmiljeniKorisnikServis;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class OmiljeniKorisnikServisImpl implements OmiljeniKorisnikServis {

    @Autowired
    private OmiljeniKorisnikRepository omiljeniKorisnikRepository;
    @Override
    public OmiljeniKorisnikDTO add(OmiljeniKorisnikDTO omiljeniKorisnikDTO) {
        return OmiljeniKorisnikMapper.toDTO(omiljeniKorisnikRepository.save(OmiljeniKorisnikMapper.toEntity(omiljeniKorisnikDTO)));
    }

    @Override
    public void edit(OmiljeniKorisnikDTO omiljeniKorisnikDTO) {
        omiljeniKorisnikRepository.save(OmiljeniKorisnikMapper.toEntity(omiljeniKorisnikDTO));
    }

    @Override
    public void delete(Long id) {
        omiljeniKorisnikRepository.deleteById(id);
    }

    @Override
    public List<OmiljeniKorisnikDTO> findByIdKorisnika(Long id) {
        return omiljeniKorisnikRepository.findOmiljeniKorisniksByIdKorisnika(id).stream().map(OmiljeniKorisnikMapper::toDTO).collect(Collectors.toList());
    }
}
