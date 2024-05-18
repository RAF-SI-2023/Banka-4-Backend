package rs.edu.raf.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SpringSecurityConfig implements WebSecurityCustomizer {

    private final JwtFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomPermissionEvaluator permissionEvaluator() {
        return new CustomPermissionEvaluator();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();//defaultni AuthenticationManager
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
//                .cors()
//                .and()
                .authorizeRequests(authorize -> authorize

                                //vazi za sve putanje u /korisnik/ ali samo sa GET metodom
                                //.requestMatchers(HttpMethod.GET,"/korisnik/**")

                                //vazi za sve putanje u /korisnik/ svih metoda
                                //.requestMatchers("/korisnik/**")

                                .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-resources/**").permitAll()
                                .requestMatchers("/korisnik/login", "/korisnik/generate-login", "/korisnik/generate-reset", "/korisnik/verifikacija", "/korisnik/reset-password", "/korisnik/id/**", "/korisnik/addAccount/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/korisnik", "/korisnik/change-password").authenticated()
                                .requestMatchers(HttpMethod.GET, "/korisnik", "/korisnik/**").access("@permissionEvaluator.hasPermission(authentication, null,'" + Permisije.list_users + "')")
                                .requestMatchers(HttpMethod.POST, "/korisnik/add").access("@permissionEvaluator.hasPermission(authentication, null,'" + Permisije.create_users + "')")
                                .requestMatchers(HttpMethod.GET, "/radnik", "/radnik/**").access("@permissionEvaluator.hasPermission(authentication, null,'" + Permisije.list_workers + "')")
                                .requestMatchers(HttpMethod.PUT, "/radnik").access("@permissionEvaluator.hasPermission(authentication, null,'" + Permisije.edit_workers + "')")
                                .requestMatchers(HttpMethod.POST, "/radnik").access("@permissionEvaluator.hasPermission(authentication, null,'" + Permisije.create_workers + "')")
//                        .requestMatchers("/racuni/dodajDevizni","/racuni/dodajPravni","/racuni/dodajTekuci").access("@permissionEvaluator.hasPermission(authentication, null,'" + Permisije.kreiranje_racuna + "')")
                                .anyRequest().authenticated()
                )
                .csrf().disable()
                .addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("http://localhost:3000");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }

    @Override
    public void customize(WebSecurity web) {
        web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }

}
