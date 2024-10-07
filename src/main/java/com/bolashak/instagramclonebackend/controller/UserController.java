package com.bolashak.instagramclonebackend.controller;

import com.bolashak.instagramclonebackend.dto.ApiResponseDto;
import com.bolashak.instagramclonebackend.dto.UserProfileUpdateRequest;
import com.bolashak.instagramclonebackend.model.User;
import com.bolashak.instagramclonebackend.repository.UserRepository;
import com.bolashak.instagramclonebackend.service.UserService;
import com.bolashak.instagramclonebackend.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PutMapping("/follow/{id}")
    public ResponseEntity<ApiResponseDto<?>> followUser(@PathVariable long id) {
        User authenticatedUser = userService.getCurrentUser();
        return userService.followUser(id, authenticatedUser);
    }
    @PutMapping("/unfollow/{id}")
    public ResponseEntity<ApiResponseDto<?>> unfollowUser(@PathVariable long id) {
        User authenticatedUser = userService.getCurrentUser();
        return userService.unfollowUser(id, authenticatedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> getUserProfile(@PathVariable long id) {
        return userService.getUserProfile(id);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<?>> searchUser(@RequestParam String query) {
        return userService.findUsersByUsername(query);
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponseDto<?>> getProfile() {
        User authenticatedUser = userService.getCurrentUser();
        return userService.getUserProfile(authenticatedUser.getId());
    }

    @PutMapping("/profile/update")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserProfileUpdateRequest request) {
        try {
            User currentUser = userService.getCurrentUser();

            currentUser.setBio(request.getBio());
            if(!request.getProfileImageUrl().isEmpty()) currentUser.setProfileImage(request.getProfileImageUrl());
            logger.info("Received request to update bio: " + request.getBio());
            logger.info("Received request to update profile image: " + request.getProfileImageUrl());

            userRepository.save(currentUser);

            return ResponseEntity.ok("Profile updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating profile");
        }
    }
}
