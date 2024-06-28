package rs.edu.raf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import rs.edu.raf.annotations.Generated;

@SpringBootApplication
@EnableScheduling
//@EnableMongoRepositories
public class BankaSpringBootApplication {
    @Generated
    public static void main(String[] args) {
        SpringApplication.run(BankaSpringBootApplication.class,args);
    }
}