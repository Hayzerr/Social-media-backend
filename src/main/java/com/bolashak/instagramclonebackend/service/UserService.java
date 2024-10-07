package com.bolashak.instagramclonebackend.service;

import com.bolashak.instagramclonebackend.dto.ApiResponseDto;
import com.bolashak.instagramclonebackend.exception.UserException;
import com.bolashak.instagramclonebackend.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User getCurrentUser();
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    void save(User user);
    ResponseEntity<ApiResponseDto<?>> followUser(Long usedId, User authenticatedUser)
            throws UserException;
    ResponseEntity<ApiResponseDto<?>> unfollowUser(Long usedId, User authenticatedUser)
            throws UserException;
    ResponseEntity<ApiResponseDto<?>> getUserProfile(Long userId) throws UserException;
    ResponseEntity<ApiResponseDto<?>> findUsersByUsername(String username) throws UserException;
}
