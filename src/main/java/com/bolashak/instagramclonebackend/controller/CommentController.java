package com.bolashak.instagramclonebackend.controller;

import com.bolashak.instagramclonebackend.dto.ApiResponseDto;
import com.bolashak.instagramclonebackend.dto.CommentRequestDto;
import com.bolashak.instagramclonebackend.model.User;
import com.bolashak.instagramclonebackend.service.CommentService;
import com.bolashak.instagramclonebackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @PostMapping("/create/{id}")
    public ResponseEntity<ApiResponseDto<?>> createComment(@RequestBody @Valid CommentRequestDto commentRequestDto, @PathVariable long id) {
        User authenticatedUser = userService.getCurrentUser();
        return commentService.createComment(commentRequestDto, id, authenticatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseDto<?>> deleteComment(@PathVariable long id) {
        User authenticatedUser = userService.getCurrentUser();
        return commentService.deleteComment(id, authenticatedUser);
    }

    @PutMapping("/like/{id}")
    public ResponseEntity<ApiResponseDto<?>> likeComment(@PathVariable long id) {
        User authenticatedUser = userService.getCurrentUser();
        return commentService.likeComment(id, authenticatedUser);
    }

    @PutMapping("/unlike/{id}")
    public ResponseEntity<ApiResponseDto<?>> unlikeComment(@PathVariable long id) {
        User authenticatedUser = userService.getCurrentUser();
        return commentService.unlikeComment(id, authenticatedUser);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponseDto<?>> getComment(@PathVariable long id) {
        return commentService.getComment(id);
    }
}
