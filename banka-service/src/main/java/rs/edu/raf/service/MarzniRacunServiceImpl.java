package rs.edu.raf.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.model.dto.racun.MarzniRacunCreateDTO;
import rs.edu.raf.model.dto.racun.MarzniRacunUpdateDTO;
import rs.edu.raf.model.entities.racun.MarzniRacun;
import rs.edu.raf.model.mapper.racun.MarzniRacunMapper;
import rs.edu.raf.repository.transaction.MarzniRacunRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class MarzniRacunServiceImpl implements MarzniRacunService{

    private MarzniRacunRepository marzniRacunRepository;


    @Override
    public ResponseEntity<?> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(marzniRacunRepository.findAll().stream().map(MarzniRacunMapper::mapToDTO).toList());
    }

    @Override
    public ResponseEntity<?> findALlByUserId(Long userId) {
       return ResponseEntity.status(HttpStatus.OK)
                .body(marzniRacunRepository.findAllByVlasnik(userId).stream().map(MarzniRacunMapper::mapToDTO).toList());
    }

    @Override
    public ResponseEntity<?> createMarzniRacun(MarzniRacunCreateDTO marzniRacunCreateDTO) {
        if (marzniRacunRepository.findByVlasnikAndGrupaHartija(marzniRacunCreateDTO.getVlasnik(), marzniRacunCreateDTO.getGrupaHartija()).isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("MarzniRacun already exists for this user and grupaHartija.");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(marzniRacunRepository.save(MarzniRacunMapper.mapToEntity(marzniRacunCreateDTO)));
    }

    @Override
    public ResponseEntity<?> changeBalance(MarzniRacunUpdateDTO marzniRacunUpdateDTO) {
        Optional<MarzniRacun> optionalMarzniRacun = marzniRacunRepository.findByVlasnikAndGrupaHartija(marzniRacunUpdateDTO.getUserId(), marzniRacunUpdateDTO.getGrupaHartija());

        if (optionalMarzniRacun.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No MarzniRacun found with the specified grupaHartija for this user.");

        MarzniRacun marzniRacun = optionalMarzniRacun.get();

        if (marzniRacun.getMarginCall() && marzniRacunUpdateDTO.getAmount().compareTo(BigDecimal.ZERO) < 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Margin call active, can't withdraw funds.");

        BigDecimal newBalance = marzniRacun.getLiquidCash().add(marzniRacunUpdateDTO.getAmount());
        marzniRacun.setLiquidCash(newBalance);

        marzniRacunRepository.save(marzniRacun);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> changeMaintenanceMargin(MarzniRacunUpdateDTO marzniRacunUpdateDTO) {
        Optional<MarzniRacun> optionalMarzniRacun = marzniRacunRepository.findByVlasnikAndGrupaHartija(marzniRacunUpdateDTO.getUserId(), marzniRacunUpdateDTO.getGrupaHartija());

        if (optionalMarzniRacun.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No MarzniRacun found with the specified grupaHartija for this user.");

        MarzniRacun marzniRacun = optionalMarzniRacun.get();
        marzniRacun.setMaintenanceMargin(marzniRacunUpdateDTO.getAmount());

        marzniRacunRepository.save(marzniRacun);
        return ResponseEntity.ok().build();
    }

    @Scheduled(cron = "0 0 12 * * ?")
    private void midDayJob() {
        marzniRacunRepository.findAll().stream()
                .filter(marzniRacun -> marzniRacun.getUlozenaSredstva().compareTo(marzniRacun.getMaintenanceMargin()) <= 0)
                .forEach(this::executeCut);
    }

    @Scheduled(cron = "0 0 21 * * ?")
    private void endOfDayJob() {
        marzniRacunRepository.findAll().stream()
                .filter(marzniRacun -> marzniRacun.getUlozenaSredstva().compareTo(marzniRacun.getMaintenanceMargin()) <= 0)
                .forEach(this::executeCut);
    }

    private void executeCut(MarzniRacun marzniRacun) {
        if (marzniRacun.getMarginCall() && marzniRacun.getMaintenanceDeadline().compareTo(System.currentTimeMillis()) >= 0) {
            liquidate(marzniRacun);
        } else {
            marzniRacun.setMarginCall(true);
            marzniRacun.setMaintenanceDeadline(System.currentTimeMillis() + 86400000L * 3L);
            marzniRacunRepository.save(marzniRacun);
        }
    }

    private void liquidate(MarzniRacun marzniRacun) {

    }

}
