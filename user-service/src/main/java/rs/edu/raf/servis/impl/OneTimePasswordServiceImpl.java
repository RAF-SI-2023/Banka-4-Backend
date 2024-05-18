package rs.edu.raf.servis.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.edu.raf.model.OneTimePassword;
import rs.edu.raf.repository.OneTimePasswordRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OneTimePasswordServiceImpl {
    @Autowired
    private OneTimePasswordRepository oneTimePasswordRepository;

    public OneTimePassword generateOneTimePassword(String email) {
        OneTimePassword otp = new OneTimePassword();
        otp.setEmail(email);
        otp.setPassword(generatePassword());
        otp.setExpiration(LocalDateTime.now().plusMinutes(5)); // 5 minutes expiration
        return oneTimePasswordRepository.save(otp);
    }

    public boolean validateOneTimePassword(String email, String password) {
        List<OneTimePassword> otpList = oneTimePasswordRepository.findByEmail(email);
        for (OneTimePassword otp : otpList) {
            if (otp.getPassword().equals(password) && otp.getExpiration().isAfter(LocalDateTime.now())) {
                oneTimePasswordRepository.delete(otp);
                return true;
            }
        }
        return false;
    }

    public void cleanupOneTimePasswords() {
        oneTimePasswordRepository.deleteByExpirationBefore(LocalDateTime.now());
    }

    private String generatePassword() {
        int length = 6;
        String characters = "0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }
}

