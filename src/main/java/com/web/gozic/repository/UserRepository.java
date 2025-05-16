package com.web.gozic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.web.gozic.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}