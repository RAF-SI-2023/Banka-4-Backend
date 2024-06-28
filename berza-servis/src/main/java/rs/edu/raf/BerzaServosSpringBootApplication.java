package rs.edu.raf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import rs.edu.raf.annotations.Generated;

@SpringBootApplication
@EnableScheduling
public class BerzaServosSpringBootApplication {
    @Generated
    public static void main(String[] args) {
        SpringApplication.run(BerzaServosSpringBootApplication.class,args);
    }

}