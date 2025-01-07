package com.sofico.sofico_backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sofico.sofico_backend.service.UserService;

@Component
public class PasswordEncryptionInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(PasswordEncryptionInitializer.class);
    private final UserService userService;

    public PasswordEncryptionInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        logger.info("Starting password encryption for existing users.");
        try {
            userService.encryptExistingPasswords();
            logger.info("Password encryption completed successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during password encryption: ", e);
        }
    }
}
