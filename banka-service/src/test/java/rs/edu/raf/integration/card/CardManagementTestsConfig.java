package rs.edu.raf.integration.card;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class CardManagementTestsConfig {

}
