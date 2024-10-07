package com.bolashak.instagramclonebackend.repository;

import com.bolashak.instagramclonebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username LIKE CONCAT('%', :username, '%')")
    List<User> findAllByUsername(String username);
}
