package rs.edu.raf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.edu.raf.model.OneTimePassword;
import rs.edu.raf.servis.impl.OneTimePasswordServiceImpl;

@RestController
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "jwt")
public class OneTimePasswordController {
    @Autowired
    private OneTimePasswordServiceImpl oneTimePasswordService;

    @Operation(description = "Generiše OneTimePassword")
    @PostMapping("/generate-otp")
    @ApiResponse(responseCode = "200", description = "password generated")
    public ResponseEntity<String> generateOneTimePassword(@RequestParam @Parameter(description = "user email") String email) {
        OneTimePassword otp = oneTimePasswordService.generateOneTimePassword(email);
        return new ResponseEntity<>(otp.getPassword(), HttpStatus.OK);
    }

    @Operation(description = "Validira OneTimePassword")
    @PostMapping("/validate-otp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "password validated"),
            @ApiResponse(responseCode = "400", description = "password not validated")
    })
    public ResponseEntity<String> validateOneTimePassword(@RequestParam @Parameter(description = "user email") String email, @RequestParam @Parameter(description = "user password") String password) {
        if (oneTimePasswordService.validateOneTimePassword(email, password)) {
            return new ResponseEntity<>("Valid OTP", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid OTP", HttpStatus.BAD_REQUEST);
        }
    }

    @Scheduled(fixedRate = 3600000) // Cleanup every hour
    public void cleanupOneTimePasswords() {
        oneTimePasswordService.cleanupOneTimePasswords();
    }
}

