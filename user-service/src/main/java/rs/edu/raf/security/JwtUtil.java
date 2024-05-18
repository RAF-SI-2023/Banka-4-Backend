package rs.edu.raf.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rs.edu.raf.model.Korisnik;
import rs.edu.raf.model.Radnik;
import rs.edu.raf.repository.KorisnikRepository;
import rs.edu.raf.repository.RadnikRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "MY JWT SECRET";
    @Autowired
    private KorisnikRepository korisnikRepository;
    @Autowired
    private RadnikRepository radnikRepository;

    public  Claims extractAllClaims(String token) {
                            //setujemo isti KEY za potpis kao pri izdavanju jer se pri uzimanju claimsa ponovo potpisuje
                            //da bi se proverilo da li je claims menjan
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String generateToken(String username){
        Optional<Korisnik> korisnik = korisnikRepository.findByEmailAndAktivanIsTrue(username);
        Map<String, Object> claims = new HashMap<>();
        if(korisnik.isPresent()) {
            claims.put("permission",0L);
            claims.put("id",korisnik.get().getId());
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
        }
        else {
            Optional<Radnik> radnik = radnikRepository.findByEmailAndAktivanIsTrue(username);
            claims.put("permission",radnik.get().getPermisije());
            claims.put("id",radnik.get().getId());
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 8))
                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
        }
    }

    public boolean validateToken(String token, UserDetails user) {
                //vec smo prethodno proverili username!!!
        return (user.getUsername().equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
