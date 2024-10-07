package com.bolashak.instagramclonebackend.controller;

import com.bolashak.instagramclonebackend.dto.ApiResponseDto;
import com.bolashak.instagramclonebackend.dto.PostRequestDto;
import com.bolashak.instagramclonebackend.exception.PostException;
import com.bolashak.instagramclonebackend.exception.UserException;
import com.bolashak.instagramclonebackend.model.User;
import com.bolashak.instagramclonebackend.service.PostService;
import com.bolashak.instagramclonebackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<ApiResponseDto<?>> getAllPosts(){
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> getPostByUserId(@PathVariable Long id)
        throws UserException{
        return postService.getPostsByUserId(id);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto<?>> createPost(@RequestBody @Valid PostRequestDto postRequestDto) {
        User authenticatedUser = userService.getCurrentUser();
        return postService.createPost(postRequestDto, authenticatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseDto<?>> deletePost(@PathVariable Long id)
        throws UserException {
        User authenticatedUser = userService.getCurrentUser();
        return postService.deletePost(id, authenticatedUser);
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<ApiResponseDto<?>> likePost(@PathVariable Long id)
    throws UserException {
        User authenticatedUser = userService.getCurrentUser();
        return postService.likePost(id, authenticatedUser);
    }

    @PostMapping("/unlike/{id}")
    public ResponseEntity<ApiResponseDto<?>> unlikePost(@PathVariable Long id)
    throws UserException, PostException {
        User authenticatedUser = userService.getCurrentUser();
        return postService.unlikePost(id, authenticatedUser);
    }

}
