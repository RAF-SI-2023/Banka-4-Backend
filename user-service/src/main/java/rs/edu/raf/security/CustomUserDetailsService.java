package rs.edu.raf.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.edu.raf.model.Korisnik;
import rs.edu.raf.model.Radnik;
import rs.edu.raf.repository.KorisnikRepository;
import rs.edu.raf.repository.RadnikRepository;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private KorisnikRepository userRepository;
    @Autowired
    private RadnikRepository radnikRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Korisnik> user = userRepository.findByEmailAndAktivanIsTrue(username);
        Optional<Radnik> radnik = radnikRepository.findByEmailAndAktivanIsTrue(username);
        if (!user.isPresent() && !radnik.isPresent()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        if(user.isPresent())
            return new org.springframework.security.core.userdetails.User(
                    user.get().getEmail(), user.get().getPassword(), user.get().getAuthorities());
        return new org.springframework.security.core.userdetails.User(
                radnik.get().getEmail(), radnik.get().getPassword(), radnik.get().getAuthorities());
    }
}
