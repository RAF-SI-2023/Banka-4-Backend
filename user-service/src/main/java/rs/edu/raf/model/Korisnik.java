package rs.edu.raf.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class Korisnik implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ime;

    private String prezime;
    @Column(unique=true)
    private String jmbg;

    private Long datumRodjenja;

    private String pol;
    @Column(unique=true)
    private String email;
    @Column(unique=true)
    private String brojTelefona;

    private String adresa;

    private String password;

    private String saltPassword;

    private String povezaniRacuni;

    private boolean aktivan;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(Long.valueOf(0).toString()));

        return authorityList;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return aktivan;
    }

    @Override
    public boolean isAccountNonLocked() {
        return aktivan;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return aktivan;
    }

    @Override
    public boolean isEnabled() {
        return aktivan;
    }
}
