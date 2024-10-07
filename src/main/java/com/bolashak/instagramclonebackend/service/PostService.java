package com.bolashak.instagramclonebackend.service;

import com.bolashak.instagramclonebackend.dto.ApiResponseDto;
import com.bolashak.instagramclonebackend.dto.PostRequestDto;
import com.bolashak.instagramclonebackend.exception.PostException;
import com.bolashak.instagramclonebackend.exception.UserException;
import com.bolashak.instagramclonebackend.model.Post;
import com.bolashak.instagramclonebackend.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    ResponseEntity<ApiResponseDto<?>> getAllPosts();
    ResponseEntity<ApiResponseDto<?>> getPostsByUserId(Long userId)
            throws UserException;
    ResponseEntity<ApiResponseDto<?>> createPost(PostRequestDto postRequestDto, User authenticatedUser);
    ResponseEntity<ApiResponseDto<?>> deletePost(Long id, User authenticatedUser)
            throws UserException;
    ResponseEntity<ApiResponseDto<?>> likePost(Long id, User authenticatedUser)
        throws PostException;
    ResponseEntity<ApiResponseDto<?>> unlikePost(Long id, User authenticatedUser)
        throws UserException, PostException;
}
