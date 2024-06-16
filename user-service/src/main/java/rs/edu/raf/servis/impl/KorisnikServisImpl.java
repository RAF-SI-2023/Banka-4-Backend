package rs.edu.raf.servis.impl;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rs.edu.raf.dto.*;
import rs.edu.raf.exceptions.JMBGDateMismatchException;
import rs.edu.raf.exceptions.UserNotFoundException;

import rs.edu.raf.exceptions.WrongEmployeeException;
import rs.edu.raf.mapper.KorisnikMapper;
import rs.edu.raf.mapper.RadnikMapper;
import rs.edu.raf.model.Korisnik;
import rs.edu.raf.model.Radnik;
import rs.edu.raf.repository.KorisnikRepository;
import rs.edu.raf.repository.RadnikRepository;
import rs.edu.raf.servis.KodServis;
import rs.edu.raf.servis.KorisnikServis;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class KorisnikServisImpl implements KorisnikServis {

    private KorisnikRepository korisnikRepository;

    private RadnikRepository radnikRepository;

    private KorisnikMapper korisnikMapper;

    private RadnikMapper radnikMapper;
    private KodServis kodServis;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public KorisnikDTO kreirajNovogKorisnika(NoviKorisnikDTO noviKorisnikDTO) {

        Korisnik korisnik = korisnikMapper.noviKorisnikDtoToKorisnik(noviKorisnikDTO);

        LocalDateTime datumRodjenja = LocalDateTime.ofInstant(Instant.ofEpochMilli(korisnik.getDatumRodjenja()), ZoneOffset.systemDefault());
        if (korisnik.getJmbg().toString().length() == 12) {
            if (datumRodjenja.getDayOfMonth() != Integer.parseInt(korisnik.getJmbg().toString().substring(0, 1)) ||
                    datumRodjenja.getMonthValue() != Integer.parseInt(korisnik.getJmbg().toString().substring(1, 3)) ||
                    datumRodjenja.getYear() % 1000 != Integer.parseInt(korisnik.getJmbg().toString().substring(3, 6))) {
                throw new JMBGDateMismatchException("Datum rodjenja i JMBG se ne poklapaju!");
            }
        }
        else{
            if (datumRodjenja.getDayOfMonth() != Integer.parseInt(korisnik.getJmbg().toString().substring(0, 2)) ||
                    datumRodjenja.getMonthValue() != Integer.parseInt(korisnik.getJmbg().toString().substring(2, 4)) ||
                    datumRodjenja.getYear() % 1000 != Integer.parseInt(korisnik.getJmbg().toString().substring(4, 7))) {
                throw new JMBGDateMismatchException("Datum rodjenja i JMBG se ne poklapaju!");
            }
        }

        return korisnikMapper.korisnikToKorisnikDto(korisnikRepository.save(korisnik));
    }

    @Override
    public boolean registrujNovogKorisnika(RegistrujKorisnikDTO registrujKorisnikDTO) {

        if(kodServis.dobarKod(registrujKorisnikDTO.getEmail(),registrujKorisnikDTO.getCode(),false)) {

            //DtoToNekiEntity ili NoviDtoToNekiEntity radimo kada ocemo da vrsimo neke operacije nad bazom za konkretan Dto sa fronta
            //NekiEntityToDto kada saljemo frontu
            Korisnik korisnik = korisnikMapper.registrujKorisnikDtoToKorisnik(registrujKorisnikDTO);
            if(korisnik == null){
                Radnik radnik = korisnikMapper.registrujRadnikDtoToRadnik(registrujKorisnikDTO);
                if(radnik == null)
                    return false;
                radnikRepository.save(radnik);
                return true;

            }
            if(korisnik.getPovezaniRacuni().contains(registrujKorisnikDTO.getBrojRacuna())) {
                                //samo azurira korisnika jer ga je vec dodao radnik u bazu
                                //sifra mora biti po Bcrypt hashovana jer se tako cuva u bazi
                korisnikRepository.save(korisnik);
                return true;
            }
        }
        return false;
    }

    @Override
    public KorisnikDTO promeniSifruKorisnikaUzKod(IzmenaSifreUzKodDto izmenaSifreUzKodDto) {
        if(kodServis.dobarKod(izmenaSifreUzKodDto.getEmail(), izmenaSifreUzKodDto.getKod(),true)) {
            Korisnik korisnik = korisnikMapper.izmenaSifreDtoToKorisnik(izmenaSifreUzKodDto);
            return korisnikMapper.korisnikToKorisnikDto(korisnikRepository.save(korisnik));
        }
        return null;
    }

    @Override
    public KorisnikDTO promeniSifruKorisnika(String email, IzmenaSifreDto izmenaSifreDto) {
        Optional<Korisnik> korisnik = korisnikRepository.findByEmailAndAktivanIsTrue(email);
        if(korisnik.isPresent()) {
            if(bCryptPasswordEncoder.matches(izmenaSifreDto.getStaraSifra(),korisnik.get().getPassword())) {
                korisnik.get().setPassword(bCryptPasswordEncoder.encode(izmenaSifreDto.getNovaSifra()));
                return korisnikMapper.korisnikToKorisnikDto(korisnikRepository.save(korisnik.get()));
            }
        }
        throw new UserNotFoundException("User with email " + email + " not found!");
    }

    @Override
    public RadnikDTO kreirajNovogRadnika(NoviRadnikDTO noviRadnikDTO, Long firmaId) {

        Radnik radnik = radnikMapper.noviRadnikDtoToRadnik(noviRadnikDTO,firmaId);

        LocalDateTime datumRodjenja = LocalDateTime.ofInstant(Instant.ofEpochMilli(radnik.getDatumRodjenja()), ZoneOffset.systemDefault());
        if (radnik.getJmbg().toString().length() == 12) {
            if (datumRodjenja.getDayOfMonth() != Integer.parseInt(radnik.getJmbg().toString().substring(0, 1)) ||
                datumRodjenja.getMonthValue() != Integer.parseInt(radnik.getJmbg().toString().substring(1, 3)) ||
                datumRodjenja.getYear() % 1000 != Integer.parseInt(radnik.getJmbg().toString().substring(3, 6))) {
                    throw new JMBGDateMismatchException("Datum rodjenja i JMBG se ne poklapaju!");
            }
        }
        else{
            if (datumRodjenja.getDayOfMonth() != Integer.parseInt(radnik.getJmbg().toString().substring(0, 2)) ||
                datumRodjenja.getMonthValue() != Integer.parseInt(radnik.getJmbg().toString().substring(2, 4)) ||
                datumRodjenja.getYear() % 1000 != Integer.parseInt(radnik.getJmbg().toString().substring(4, 7))) {
                    throw new JMBGDateMismatchException("Datum rodjenja i JMBG se ne poklapaju!");
            }
        }

        return radnikMapper.radnikToRadnikDto(radnikRepository.save(radnik));
    }

    @Override
    public KorisnikDTO izmeniKorisnika(IzmenaKorisnikaDTO izmenaKorisnikaDTO) {

        Optional<Korisnik> korisnik = korisnikRepository.findById(izmenaKorisnikaDTO.getId());

        if (korisnik.isPresent()) {
            LocalDateTime datumRodjenja = LocalDateTime.ofInstant(Instant.ofEpochMilli(korisnik.get().getDatumRodjenja()), ZoneOffset.systemDefault());
            if (korisnik.get().getJmbg().toString().length() == 12) {
                if (datumRodjenja.getDayOfMonth() != Integer.parseInt(korisnik.get().getJmbg().toString().substring(0, 1)) ||
                        datumRodjenja.getMonthValue() != Integer.parseInt(korisnik.get().getJmbg().toString().substring(1, 3)) ||
                        datumRodjenja.getYear() % 1000 != Integer.parseInt(korisnik.get().getJmbg().toString().substring(3, 6))) {
                    throw new JMBGDateMismatchException("Datum rodjenja i JMBG se ne poklapaju!");
                }
            }
            else{
                if (datumRodjenja.getDayOfMonth() != Integer.parseInt(korisnik.get().getJmbg().toString().substring(0, 2)) ||
                        datumRodjenja.getMonthValue() != Integer.parseInt(korisnik.get().getJmbg().toString().substring(2, 4)) ||
                        datumRodjenja.getYear() % 1000 != Integer.parseInt(korisnik.get().getJmbg().toString().substring(4, 7))) {
                    throw new JMBGDateMismatchException("Datum rodjenja i JMBG se ne poklapaju!");
                }
            }

            if(izmenaKorisnikaDTO.getPrezime()!=null && !izmenaKorisnikaDTO.getPrezime().equals(""))
                korisnik.get().setPrezime(izmenaKorisnikaDTO.getPrezime());
            if(izmenaKorisnikaDTO.getPol()!=null && !izmenaKorisnikaDTO.getPol().equals("") && (izmenaKorisnikaDTO.getPol().equalsIgnoreCase("M")|| izmenaKorisnikaDTO.getPol().equalsIgnoreCase("Z")))
                korisnik.get().setPol(izmenaKorisnikaDTO.getPol());
            if(izmenaKorisnikaDTO.getEmail()!=null && !izmenaKorisnikaDTO.getEmail().equals(""))
                korisnik.get().setEmail(izmenaKorisnikaDTO.getEmail());
            if(izmenaKorisnikaDTO.getBrojTelefona()!=null && !izmenaKorisnikaDTO.getBrojTelefona().equals(""))
                korisnik.get().setBrojTelefona(izmenaKorisnikaDTO.getBrojTelefona());
            if(izmenaKorisnikaDTO.getAdresa()!=null && !izmenaKorisnikaDTO.getAdresa().equals(""))
                korisnik.get().setAdresa(izmenaKorisnikaDTO.getAdresa());
            if(izmenaKorisnikaDTO.getPassword()!=null && !izmenaKorisnikaDTO.getPassword().equals(""))
                korisnik.get().setPassword(bCryptPasswordEncoder.encode(izmenaKorisnikaDTO.getPassword()));
            if(izmenaKorisnikaDTO.getPovezaniRacuni()!=null && !izmenaKorisnikaDTO.getPovezaniRacuni().equals(""))
                korisnik.get().setPovezaniRacuni(izmenaKorisnikaDTO.getPovezaniRacuni());
            if(izmenaKorisnikaDTO.getAktivan()!=null)
                korisnik.get().setAktivan(izmenaKorisnikaDTO.getAktivan());

            return korisnikMapper.korisnikToKorisnikDto(korisnikRepository.save(korisnik.get()));
        }
        throw new UserNotFoundException("User with id " + izmenaKorisnikaDTO.getId() + " not found!");
    }

    @Override
    public RadnikDTO izmeniRadnika(IzmenaRadnikaDTO izmenaRadnikaDTO) {

        Optional<Radnik> radnik = radnikRepository.findById(izmenaRadnikaDTO.getId());

        if (radnik.isPresent()) {
            LocalDateTime datumRodjenja = LocalDateTime.ofInstant(Instant.ofEpochMilli(radnik.get().getDatumRodjenja()), ZoneOffset.systemDefault());
            if (radnik.get().getJmbg().toString().length() == 12) {
                if (datumRodjenja.getDayOfMonth() != Integer.parseInt(radnik.get().getJmbg().toString().substring(0, 1)) ||
                        datumRodjenja.getMonthValue() != Integer.parseInt(radnik.get().getJmbg().toString().substring(1, 3)) ||
                        datumRodjenja.getYear() % 1000 != Integer.parseInt(radnik.get().getJmbg().toString().substring(3, 6))) {
                    throw new JMBGDateMismatchException("Datum rodjenja i JMBG se ne poklapaju!");
                }
            }
            else{
                if (datumRodjenja.getDayOfMonth() != Integer.parseInt(radnik.get().getJmbg().toString().substring(0, 2)) ||
                        datumRodjenja.getMonthValue() != Integer.parseInt(radnik.get().getJmbg().toString().substring(2, 4)) ||
                        datumRodjenja.getYear() % 1000 != Integer.parseInt(radnik.get().getJmbg().toString().substring(4, 7))) {
                    throw new JMBGDateMismatchException("Datum rodjenja i JMBG se ne poklapaju!");
                }
            }

            if(izmenaRadnikaDTO.getPrezime()!=null && !izmenaRadnikaDTO.getPrezime().equals(""))
                radnik.get().setPrezime(izmenaRadnikaDTO.getPrezime());
            if(izmenaRadnikaDTO.getPol()!=null && !izmenaRadnikaDTO.getPol().equals("") && (izmenaRadnikaDTO.getPol().equalsIgnoreCase("M")|| izmenaRadnikaDTO.getPol().equalsIgnoreCase("Z")))
                radnik.get().setPol(izmenaRadnikaDTO.getPol());
            if(izmenaRadnikaDTO.getBrojTelefona()!=null && !izmenaRadnikaDTO.getBrojTelefona().equals(""))
                radnik.get().setBrojTelefona(izmenaRadnikaDTO.getBrojTelefona());
            if(izmenaRadnikaDTO.getPassword()!=null && !izmenaRadnikaDTO.getPassword().equals(""))
                radnik.get().setPassword(bCryptPasswordEncoder.encode(izmenaRadnikaDTO.getPassword()));
            radnik.get().setPozicija(izmenaRadnikaDTO.getPozicija());
            if(izmenaRadnikaDTO.getDepartman()!=null && !izmenaRadnikaDTO.getDepartman().equals(""))
                radnik.get().setDepartman(izmenaRadnikaDTO.getDepartman());
            if(izmenaRadnikaDTO.getPermisije()!=null)
                radnik.get().setPermisije(izmenaRadnikaDTO.getPermisije());
            if(izmenaRadnikaDTO.getAdresa()!=null)
                radnik.get().setAdresa(izmenaRadnikaDTO.getAdresa());
            radnik.get().setAktivan(izmenaRadnikaDTO.getAktivan());
            radnik.get().setSupervisor(izmenaRadnikaDTO.isSupervisor());
            radnik.get().setDailyLimit(izmenaRadnikaDTO.getDailyLimit());
            radnik.get().setApprovalFlag(izmenaRadnikaDTO.isApprovalFlag());

            return radnikMapper.radnikToRadnikDto(radnikRepository.save(radnik.get()));
        }

        throw new UserNotFoundException("Employee with id " + izmenaRadnikaDTO.getId() + " not found!");
    }

    @Override
    public List<RadnikDTO> izlistajSveAktivneRadnike() {

        List<Radnik> radnici = radnikRepository.findAllByAktivanIsTrue();
        radnici.forEach(System.out::println);

        return radnici.stream().map(radnikMapper::radnikToRadnikDto).collect(Collectors.toList());
    }

    @Override
    public List<KorisnikDTO> izlistajSveAktivneKorisnike() {

        List<Korisnik> korisnici = korisnikRepository.findAllByAktivanIsTrue();

        return korisnici.stream().map(korisnikMapper::korisnikToKorisnikDto).collect(Collectors.toList());
    }

    @Override
    public RadnikDTO nadjiAktivnogRadnikaPoEmail(String email) {

        Optional<Radnik> radnik = radnikRepository.findByEmailAndAktivanIsTrue(email);

        return radnik.map(radnikMapper::radnikToRadnikDto).orElseThrow(()->new UserNotFoundException("Employee with phone " + email + " not found!"));
    }

    @Override
    public KorisnikDTO nadjiAktivnogKorisnikaPoEmail(String email) {

        Optional<Korisnik> korisnik = korisnikRepository.findByEmailAndAktivanIsTrue(email);

        return korisnik.map(korisnikMapper::korisnikToKorisnikDto).orElseThrow(()->new UserNotFoundException("User with email " + email + " not found!"));
    }

    @Override
    public KorisnikDTO nadjiAktivnogKorisnikaPoJMBG(String jmbg) {

        Optional<Korisnik> korisnik = korisnikRepository.findByJmbgAndAktivanIsTrue(jmbg);

        return korisnik.map(korisnikMapper::korisnikToKorisnikDto).orElseThrow(()->new UserNotFoundException("User with jmbg " + jmbg + " not found!"));
    }

    @Override
    public KorisnikDTO nadjiAktivnogKorisnikaPoBrojuTelefona(String brojTelefona) {

        Optional<Korisnik> korisnik = korisnikRepository.findByBrojTelefonaAndAktivanIsTrue(brojTelefona);

        return korisnik.map(korisnikMapper::korisnikToKorisnikDto).orElseThrow(()->new UserNotFoundException("User with phone " + brojTelefona + " not found!"));
    }

    @Override
    public KorisnikDTO findUserById(Long id) {
        Korisnik korisnik = korisnikRepository.findKorisnikByIdAndAktivanIsTrue(id).orElse(null);
        if(korisnik != null) return korisnikMapper.korisnikToKorisnikDto(korisnik) ;
        return null;
    }

    public RadnikDTO findWorkerById(Long id){
        return radnikMapper.radnikToRadnikDto(radnikRepository.findById(id).orElse(null));
    }

    @Override
    public boolean addAccountToUser(Long userId, Long accountNumber) {
        Optional<Korisnik> optionalKorisnik = korisnikRepository.findKorisnikByIdAndAktivanIsTrue(userId);
        if(optionalKorisnik.isPresent()){

            Korisnik korisnik = optionalKorisnik.get();

            if(korisnik.getPovezaniRacuni() == null){
                korisnik.setPovezaniRacuni(accountNumber.toString());
                korisnikRepository.save(korisnik);
                return true;
            }

            if(korisnik.getPovezaniRacuni().contains(accountNumber.toString())){
                return false;
            }

            korisnik.setPovezaniRacuni(korisnik.getPovezaniRacuni() + "," + accountNumber.toString());
            korisnikRepository.save(korisnik);
            return true;
        }
        return false;
    }

    @Override
    public RadnikDTO resetLimit(Long radnikId, Long id) {
        Radnik radnik = radnikRepository.findById(radnikId).orElseThrow(()->new UserNotFoundException("Employee with id " + radnikId + " not found!"));
        if(radnik.getFirmaId() != id) throw new WrongEmployeeException("That employee isn't in your company!");
        radnik.setDailySpent(BigDecimal.ZERO);
        return radnikMapper.radnikToRadnikDto(radnikRepository.save(radnik));
    }

    @Override
    public void updateDailySpent(Long id, BigDecimal price) {
        Radnik radnik = radnikRepository.findById(id).orElseThrow(()->new UserNotFoundException("Employee with id " + id + " not found!"));
        radnik.setDailySpent(radnik.getDailySpent().add(price));
        radnikRepository.save(radnik);
    }

    @Scheduled(initialDelay = 180000, fixedRate = 60000)
    public void clearDailySpent() {
        radnikRepository.clearDailySpent();
    }
}

