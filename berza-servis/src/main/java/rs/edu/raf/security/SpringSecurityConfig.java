package rs.edu.raf.security;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
//@AllArgsConstructor
public class SpringSecurityConfig implements WebSecurityCustomizer {


    private final JwtFilter jwtFilter;

    @Autowired
    public SpringSecurityConfig(JwtFilter jwtFilter){
        this.jwtFilter = jwtFilter;
    }

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
        return config.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**","/api-docs/**","/swagger-resources/**").permitAll()
                        .requestMatchers("/futures/name/**","/futures/type/**").permitAll()
                        .requestMatchers("/offer/place-offer").permitAll()
                        .requestMatchers("/offer/accept-our-offer").permitAll()
                        .requestMatchers("/user-stocks/get-our-banks-stocks").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/racuni/izlistajSveFirme").access("@permissionEvaluator.hasPermission(authentication, null,'" + Permisije.listanje_korisnika + "')")
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
