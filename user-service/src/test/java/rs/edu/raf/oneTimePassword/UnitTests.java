package rs.edu.raf.oneTimePassword;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class UnitTests {
/*
    @Autowired
    private OneTimePasswordRepository oneTimePasswordRepository;

    @MockBean
    private OneTimePasswordService oneTimePasswordService;

    @Test
    public void whenGenerateOneTimePassword_thenPasswordIsGeneratedAndSaved() {
        String email = "test@example.com";
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
        oneTimePasswordRepository.save(otp);

        boolean result = oneTimePasswordService.validateOneTimePassword(email, password);

        assertTrue(result);
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
        oneTimePasswordRepository.save(otp);

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

        List<OneTimePassword> remaining = oneTimePasswordRepository.findAll();
        assertEquals(1, remaining.size());
        assertEquals(otp2, remaining.get(0));
    }
    */
}
