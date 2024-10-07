package com.bolashak.instagramclonebackend.service.impl;

import com.bolashak.instagramclonebackend.dto.*;
import com.bolashak.instagramclonebackend.exception.CommentException;
import com.bolashak.instagramclonebackend.exception.PostException;
import com.bolashak.instagramclonebackend.exception.UserException;
import com.bolashak.instagramclonebackend.model.Comment;
import com.bolashak.instagramclonebackend.model.Post;
import com.bolashak.instagramclonebackend.model.User;
import com.bolashak.instagramclonebackend.repository.CommentRepository;
import com.bolashak.instagramclonebackend.repository.PostRepository;
import com.bolashak.instagramclonebackend.service.CommentService;
import com.bolashak.instagramclonebackend.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class CommentServiceImpl implements CommentService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<ApiResponseDto<?>> createComment(CommentRequestDto commentRequestDto, Long postId, User authenticatedUser)
        throws PostException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(String.format("Post with ID %d not found", postId)));
        Comment comment = Comment.builder()
                .content(commentRequestDto.getContent())
                .user(authenticatedUser)
                .createdAt(LocalDateTime.now())
                .post(post)
                .build();
        Comment createdComment = commentRepository.save(comment);
        post.getComments().add(createdComment);
        postRepository.save(post);
        CommentDto commentDto = modelMapper.map(createdComment, CommentDto.class);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("Comment created succesfully")
                                .response(commentDto)
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> deleteComment(Long commentId, User authenticatedUser)
        throws CommentException, UserException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(String.format("Comment with ID %d not found", commentId)));
       if(!comment.getUser().getId().equals(authenticatedUser.getId())) {
           throw new UserException(String.format("Can't delete the comment of the other user with ID %d", authenticatedUser.getId()));
       }
        commentRepository.deleteById(commentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("Comment deleted successfully")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> likeComment(Long commentId, User authenticatedUser)
        throws CommentException, UserException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(String.format("Comment with ID %d not found", commentId)));
        if(comment.getLikedByUsers().contains(authenticatedUser))
            throw new UserException("You already liked the comment");
        comment.getLikedByUsers().add(authenticatedUser);
        commentRepository.save(comment);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("Liked comment successfully")
                                .build()
                );
    }


    @Override
    public ResponseEntity<ApiResponseDto<?>> unlikeComment(Long commentId, User authenticatedUser)
            throws CommentException, UserException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(String.format("Comment with ID %d not found", commentId)));
        if(!comment.getLikedByUsers().contains(authenticatedUser))
            throw new UserException("You didnt like the comment to unlike it");
        comment.getLikedByUsers().remove(authenticatedUser);
        commentRepository.save(comment);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("Liked comment successfully")
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getComment(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(String.format("Cant find the comment with ID %d", commentId)));
        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("Comment successfully retrieved")
                                .response(commentDto)
                                .build()
                );
    }

}
