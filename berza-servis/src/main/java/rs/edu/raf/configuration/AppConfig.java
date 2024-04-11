package rs.edu.raf.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import rs.edu.raf.opcija.servis.IzvedeneVrednostiUtil;
import rs.edu.raf.opcija.servis.util.FinansijaApiUtil;
import rs.edu.raf.opcija.servis.util.JsonParserUtil;

@Configuration
public class AppConfig {

    @Bean
    @Scope("singleton")
    public FinansijaApiUtil finansijaApiUtil() {
        return new FinansijaApiUtil();
    }
    @Bean
    @Scope("singleton")
    public JsonParserUtil jsonParserUtil() {
        return new JsonParserUtil();
    }
    @Bean
    @Scope("singleton")
    public IzvedeneVrednostiUtil izvedeneVrednostiUtil() {
        return new IzvedeneVrednostiUtil();
    }


    //in memory cache
    @Bean
    @Scope("singleton")
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("opcijeCache");
    }

}