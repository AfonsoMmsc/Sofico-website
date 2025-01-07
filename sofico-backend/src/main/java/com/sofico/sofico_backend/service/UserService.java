package com.sofico.sofico_backend.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sofico.sofico_backend.models.User;
import com.sofico.sofico_backend.repository.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void encryptExistingPasswords() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            String password = user.getPassword();

            if (!password.startsWith("$2a$")) { // Standard BCrypt prefix
                String encodedPassword = passwordEncoder.encode(password);
                user.setPassword(encodedPassword);
                userRepository.save(user);
                logger.info("Encrypted password for user: {}", user.getUsername());
            } else {
                logger.info("Password for user {} is already encrypted", user.getUsername());
            }
        }
    }
}