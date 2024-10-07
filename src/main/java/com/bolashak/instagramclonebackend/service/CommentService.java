package com.bolashak.instagramclonebackend.service;

import com.bolashak.instagramclonebackend.dto.ApiResponseDto;
import com.bolashak.instagramclonebackend.dto.CommentRequestDto;
import com.bolashak.instagramclonebackend.exception.CommentException;
import com.bolashak.instagramclonebackend.exception.PostException;
import com.bolashak.instagramclonebackend.exception.UserException;
import com.bolashak.instagramclonebackend.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    ResponseEntity<ApiResponseDto<?>> createComment(CommentRequestDto commentRequestDto, Long postId, User authenticatedUser)
            throws PostException;
    ResponseEntity<ApiResponseDto<?>> deleteComment(Long commentId, User authenticatedUser)
            throws CommentException, UserException;
    ResponseEntity<ApiResponseDto<?>> likeComment(Long commentId, User authenticatedUser)
            throws CommentException;
    ResponseEntity<ApiResponseDto<?>> unlikeComment(Long commentId, User authenticatedUser)
            throws CommentException;
    ResponseEntity<ApiResponseDto<?>> getComment(Long commentId)
            throws CommentException;
}
