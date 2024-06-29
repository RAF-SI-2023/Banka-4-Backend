package rs.edu.raf.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class Radnik implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ime;

    private String prezime;
    @Column(unique=true)
    private String jmbg;

    private Long datumRodjenja;

    private BigDecimal profit;

    private String pol;
    @Column(unique=true)
    private String email;
    @Column(unique=true)
    private String brojTelefona;

    private String adresa;

    private String username;

    private String password;

    private String saltPassword;

    private String pozicija;

    private String departman;

    private Long permisije;

    private boolean aktivan;
    private Long firmaId;
    private BigDecimal dailyLimit;
    private BigDecimal dailySpent;
    private boolean approvalFlag;
    private boolean supervisor;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(permisije.toString()));

        return authorityList;
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
