package com.bolashak.instagramclonebackend.service.impl;

import com.bolashak.instagramclonebackend.controller.FileUploadController;
import com.bolashak.instagramclonebackend.dto.ApiResponseDto;
import com.bolashak.instagramclonebackend.dto.PostDto;
import com.bolashak.instagramclonebackend.dto.UserDto;
import com.bolashak.instagramclonebackend.dto.UserProfileResponseDto;
import com.bolashak.instagramclonebackend.exception.UserException;
import com.bolashak.instagramclonebackend.model.User;
import com.bolashak.instagramclonebackend.repository.UserRepository;
import com.bolashak.instagramclonebackend.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> findUsersByUsername(String username){
        List<User> users = userRepository.findAllByUsername(username);
        if(users.isEmpty()){
            throw new UserException("Users not found");
        }
        List<UserDto> userDtos = users.stream().map(u -> modelMapper.map(u, UserDto.class)).collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("Users retrieved successfully")
                                .response(userDtos)
                                .build()
                );
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            // Fetch user from the database using the username
            return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }
        return null;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> followUser(Long userId, User authenticatedUser)
        throws UserException {
        User followingUser = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));

        logger.info("Current user: {}", authenticatedUser);
        logger.info("Following user: {}", followingUser.getFollowers());
        if(authenticatedUser.equals(followingUser)){
            throw new UserException("Cant follow yourself");
        }
        if(authenticatedUser.getFollowings().contains(followingUser)){
            throw new UserException("You already following him");
        }
        authenticatedUser.getFollowings().add(followingUser);
        followingUser.getFollowers().add(authenticatedUser);
        userRepository.save(authenticatedUser);
        User user = userRepository.save(followingUser);
        UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto();
        //logger.info("User retrieved: {}", user);
        userProfileResponseDto.setId(user.getId());
        userProfileResponseDto.setUsername(user.getUsername());
        userProfileResponseDto.setUserImage(user.getProfileImage());
        userProfileResponseDto.setBio(user.getBio());


        UserDto userDto = modelMapper.map(user, UserDto.class);
        // Convert posts to PostDto
        List<PostDto> postDtos = user.getPosts().stream()
                .map(post -> {
                    PostDto postDto = new PostDto();
                    postDto.setId(post.getId());
                    postDto.setImageUrl(post.getImageUrl());
                    postDto.setCaption(post.getCaption());
                    postDto.setUser(userDto);
                    return postDto;
                })
                .collect(Collectors.toList());

        userProfileResponseDto.setPosts(postDtos);
        List<UserDto> followersDto = user.getFollowers().stream()
                .map(follower -> {
                    UserDto followerDto = new UserDto();
                    followerDto.setBio(follower.getBio());
                    followerDto.setUserImage(follower.getProfileImage());
                    followerDto.setId(follower.getId());
                    followerDto.setUsername(follower.getUsername());
                    return followerDto;
                }).collect(Collectors.toList());
        userProfileResponseDto.setFollowers(followersDto);

        List<UserDto> followingsDto = user.getFollowings().stream()
                .map(following -> {
                    UserDto followingDto = new UserDto();
                    followingDto.setBio(following.getBio());
                    followingDto.setUserImage(following.getProfileImage());
                    followingDto.setId(following.getId());
                    followingDto.setUsername(following.getUsername());
                    return followingDto;
                }).collect(Collectors.toList());
        userProfileResponseDto.setFollowings(followingsDto);
        logger.info("User profile: {}", userProfileResponseDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("Followed to user successfully")
                                .response(userProfileResponseDto)
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> unfollowUser(Long userId, User authenticatedUser){
        User UnfollowingUser = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
        if(authenticatedUser.equals(UnfollowingUser)){
            throw new UserException("Cant unfollow yourself");
        }
        if(!authenticatedUser.getFollowings().contains(UnfollowingUser)){
            throw new UserException("You already not followed to this user");
        }
        authenticatedUser.getFollowings().remove(UnfollowingUser);
        UnfollowingUser.getFollowers().remove(authenticatedUser);
        User user = userRepository.save(UnfollowingUser);
        userRepository.save(authenticatedUser);

        UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto();
        //logger.info("User retrieved: {}", user);
        userProfileResponseDto.setId(user.getId());
        userProfileResponseDto.setUsername(user.getUsername());
        userProfileResponseDto.setUserImage(user.getProfileImage());
        userProfileResponseDto.setBio(user.getBio());


        UserDto userDto = modelMapper.map(user, UserDto.class);
        // Convert posts to PostDto
        List<PostDto> postDtos = user.getPosts().stream()
                .map(post -> {
                    PostDto postDto = new PostDto();
                    postDto.setId(post.getId());
                    postDto.setImageUrl(post.getImageUrl());
                    postDto.setCaption(post.getCaption());
                    postDto.setUser(userDto);
                    return postDto;
                })
                .collect(Collectors.toList());

        userProfileResponseDto.setPosts(postDtos);
        List<UserDto> followersDto = user.getFollowers().stream()
                .map(follower -> {
                    UserDto followerDto = new UserDto();
                    followerDto.setBio(follower.getBio());
                    followerDto.setUserImage(follower.getProfileImage());
                    followerDto.setId(follower.getId());
                    followerDto.setUsername(follower.getUsername());
                    return followerDto;
                }).collect(Collectors.toList());
        userProfileResponseDto.setFollowers(followersDto);

        List<UserDto> followingsDto = user.getFollowings().stream()
                .map(following -> {
                    UserDto followingDto = new UserDto();
                    followingDto.setBio(following.getBio());
                    followingDto.setUserImage(following.getProfileImage());
                    followingDto.setId(following.getId());
                    followingDto.setUsername(following.getUsername());
                    return followingDto;
                }).collect(Collectors.toList());
        userProfileResponseDto.setFollowings(followingsDto);
        logger.info("User profile: {}", userProfileResponseDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("Unfollowed from user successfully")
                                .response(userProfileResponseDto)
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getUserProfile(Long userId)
        throws UserException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserException("User not found"));
        UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto();
        //logger.info("User retrieved: {}", user);
        userProfileResponseDto.setId(user.getId());
        userProfileResponseDto.setUsername(user.getUsername());
        userProfileResponseDto.setUserImage(user.getProfileImage());
        userProfileResponseDto.setBio(user.getBio());


        UserDto userDto = modelMapper.map(user, UserDto.class);
        // Convert posts to PostDto
        List<PostDto> postDtos = user.getPosts().stream()
                .map(post -> {
                    PostDto postDto = new PostDto();
                    postDto.setId(post.getId());
                    postDto.setImageUrl(post.getImageUrl());
                    postDto.setCaption(post.getCaption());
                    postDto.setUser(userDto);
                    return postDto;
                })
                .collect(Collectors.toList());

        userProfileResponseDto.setPosts(postDtos);
        List<UserDto> followersDto = user.getFollowers().stream()
                        .map(follower -> {
                            UserDto followerDto = new UserDto();
                            followerDto.setBio(follower.getBio());
                            followerDto.setUserImage(follower.getProfileImage());
                            followerDto.setId(follower.getId());
                            followerDto.setUsername(follower.getUsername());
                            return followerDto;
                        }).collect(Collectors.toList());
        userProfileResponseDto.setFollowers(followersDto);

        List<UserDto> followingsDto = user.getFollowings().stream()
                .map(following -> {
                    UserDto followingDto = new UserDto();
                    followingDto.setBio(following.getBio());
                    followingDto.setUserImage(following.getProfileImage());
                    followingDto.setId(following.getId());
                    followingDto.setUsername(following.getUsername());
                    return followingDto;
                }).collect(Collectors.toList());
        userProfileResponseDto.setFollowings(followingsDto);

        logger.info("User profile: {}", userProfileResponseDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("User retrieved successfully")
                                .response(userProfileResponseDto)
                                .build()
                );
    }
}
