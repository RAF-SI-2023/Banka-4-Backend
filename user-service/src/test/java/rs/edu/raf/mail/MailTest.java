package rs.edu.raf.mail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rs.edu.raf.dto.KorisnikDTO;
import rs.edu.raf.servis.impl.MailServiceImplementation;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MailTest {

    @Autowired
    MailServiceImplementation mailServiceImplementation;

    @Test
    public void posaljiMailZaRegistracijuTest(){
        KorisnikDTO kor = new KorisnikDTO();
        kor.setPol("Z");
        kor.setPrezime("Rakic");
        kor.setEmail("abuncic720rn@raf.rs");
        assertTrue(mailServiceImplementation.posaljiMailZaRegistraciju(kor, "123456"));
    }

    @Test
    public void posaljiMailZaPromenuLozinkeTest(){
        KorisnikDTO kor = new KorisnikDTO();
        kor.setPol("Z");
        kor.setPrezime("Rakic");
        kor.setEmail("abuncic720rn@raf.rs");

        assertTrue(mailServiceImplementation.posaljiMailZaPromenuLozinke(kor, "123456"));
    }


}
