package rs.edu.raf.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//OBAVEZNO ZA PRISTUP h2 BAZI
@Configuration
@EnableWebSecurity
class SecurityConfiguration implements WebSecurityCustomizer{

    @Override
    public void customize(WebSecurity web) {
        web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }                                                               //,new AntPathRequestMatcher("/opcija/**")
}