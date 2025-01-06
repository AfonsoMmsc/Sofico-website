package com.sofico.sofico_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sofico.sofico_backend.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
