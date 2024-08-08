package rs.edu.raf.korisnik;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import rs.edu.raf.exceptions.InvalidTokenException;
import rs.edu.raf.model.Kod;
import rs.edu.raf.repository.KodRepository;
import rs.edu.raf.servis.impl.KodServisImpl;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KodServisUnitTests {

    @Mock
    private KodRepository kodRepository;

    @InjectMocks
    private KodServisImpl kodServis;

    @Test
    public void testKodNePostoji() {
        when(kodRepository.findByEmailAndReset(any(),anyBoolean())).thenReturn(Optional.empty());
        assertThrows(InvalidTokenException.class,()->kodServis.dobarKod("email@gmail.com","123456",true));
    }

    @Test
    public void testPogresanKod() {
        Kod k = new Kod();
        k.setExpirationDate(System.currentTimeMillis());
        k.setEmail("email@gmail.com");
        k.setCode("123456");
        k.setReset(true);
        when(kodRepository.findByEmailAndReset(any(),anyBoolean())).thenReturn(Optional.of(k));
        assertThrows(InvalidTokenException.class,()->kodServis.dobarKod("email@gmail.com","123457",true));
    }

    @Test
    public void testKodPostojiIstekao() {
        Kod k = new Kod();
        k.setExpirationDate(System.currentTimeMillis() - 1000);
        k.setEmail("email@gmail.com");
        k.setCode("123456");
        k.setReset(true);
        when(kodRepository.findByEmailAndReset(any(),anyBoolean())).thenReturn(Optional.of(k));
        assertThrows(InvalidTokenException.class,()->kodServis.dobarKod("email@gmail.com","123456",true));
    }

    @Test
    public void testDobarKod() {
        Kod k = new Kod();
        k.setExpirationDate(System.currentTimeMillis() + 60 * 60 * 1000);
        k.setEmail("email@gmail.com");
        k.setCode("123456");
        k.setReset(true);
        when(kodRepository.findByEmailAndReset(any(),anyBoolean())).thenReturn(Optional.of(k));
        assertTrue(kodServis.dobarKod("email@gmail.com","123456",true));
    }
}
