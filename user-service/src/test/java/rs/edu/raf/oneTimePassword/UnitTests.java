package rs.edu.raf.oneTimePassword;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import rs.edu.raf.model.OneTimePassword;
import rs.edu.raf.repository.OneTimePasswordRepository;
import rs.edu.raf.servis.impl.OneTimePasswordServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UnitTests {

    @Mock
    private OneTimePasswordRepository oneTimePasswordRepository;

    @InjectMocks
    private OneTimePasswordServiceImpl oneTimePasswordService;

    @Test
    public void whenGenerateOneTimePassword_thenPasswordIsGeneratedAndSaved() {
        String email = "test@example.com";
        when(oneTimePasswordRepository.save(any())).thenReturn(new OneTimePassword(1L,email,"123456",LocalDateTime.now().plusMinutes(5)));
        OneTimePassword otp = oneTimePasswordService.generateOneTimePassword(email);
        assertEquals(email, otp.getEmail());
        assertEquals(6, otp.getPassword().length());
        assertTrue(otp.getExpiration().isAfter(LocalDateTime.now()));
    }

    @Test
    public void whenValidateOneTimePassword_thenValidPasswordIsDeletedAndTrueReturned() {
        String email = "test@example.com";
        String password = "123456";
        OneTimePassword otp = new OneTimePassword();
        otp.setEmail(email);
        otp.setPassword(password);
        otp.setExpiration(LocalDateTime.now().plusMinutes(5));
        when(oneTimePasswordRepository.findByEmail(email)).thenReturn(List.of(otp));

        boolean result = oneTimePasswordService.validateOneTimePassword(email, password);

        assertTrue(result);
        when(oneTimePasswordRepository.findByEmail(email)).thenReturn(List.of());
        List<OneTimePassword> remaining = oneTimePasswordRepository.findByEmail(email);
        assertEquals(0, remaining.size());
    }

    @Test
    public void whenValidateOneTimePassword_thenExpiredPasswordIsNotDeletedAndFalseReturned() {
        String email = "test@example.com";
        String password = "123456";
        OneTimePassword otp = new OneTimePassword();
        otp.setEmail(email);
        otp.setPassword(password);
        otp.setExpiration(LocalDateTime.now().minusMinutes(1)); // Expired
        when(oneTimePasswordRepository.findByEmail(email)).thenReturn(List.of(otp));

        boolean result = oneTimePasswordService.validateOneTimePassword(email, password);

        assertFalse(result);
        List<OneTimePassword> remaining = oneTimePasswordRepository.findByEmail(email);
        assertEquals(1, remaining.size());
    }

    @Test
    public void whenCleanupOneTimePasswords_thenExpiredPasswordsAreDeleted() {
        OneTimePassword otp1 = new OneTimePassword();
        otp1.setExpiration(LocalDateTime.now().minusMinutes(10)); // Expired
        oneTimePasswordRepository.save(otp1);

        OneTimePassword otp2 = new OneTimePassword();
        otp2.setExpiration(LocalDateTime.now().plusMinutes(10)); // Not expired
        oneTimePasswordRepository.save(otp2);
        oneTimePasswordService.cleanupOneTimePasswords();

        verify(oneTimePasswordRepository, times(1)).deleteByExpirationBefore(any());
    }
}
