package rs.edu.raf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class UserBackendSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserBackendSpringBootApplication.class,args);
    }

}
