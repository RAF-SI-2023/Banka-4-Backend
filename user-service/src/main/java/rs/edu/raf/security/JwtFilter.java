package rs.edu.raf.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService userService;
    private final JwtUtil jwtUtil;

    public JwtFilter(CustomUserDetailsService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = httpServletRequest.getHeader("Authorization");
        String jwt = null;
        String username = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);

                            //pri uzimanju claimsa se proverava potpis
            username = jwtUtil.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                                    //uzimamo podatke usera da bismo ih nakon provere jwta stavili u obliku userDetails u UsernamePasswordAuthenticationToken i globalno mu pristupali iz servisa odnosno kao da je user ulogovan u nas servis
                                    //takodje UserDetails je dostupan i u authentication objektu koji je nastao nakon provere jwt-a iz jwtFiltera
                        //userDetails sadrzi podatke(email,password,prava) usera iz baze a pronasli smo ga preko username iz tokena
            UserDetails userDetails = this.userService.loadUserByUsername(username);

                        //provera datum isteka
            if (jwtUtil.validateToken(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                                    //setujemo da je ulogovan u nasem servisu odnosno globalno se moze pristupiti njegovim podacima
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        //nastavljamo lanac filtera
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
