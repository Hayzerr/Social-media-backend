package com.bolashak.instagramclonebackend.service.impl;

import com.bolashak.instagramclonebackend.dto.ApiResponseDto;
import com.bolashak.instagramclonebackend.dto.PostDto;
import com.bolashak.instagramclonebackend.dto.PostRequestDto;
import com.bolashak.instagramclonebackend.exception.PostException;
import com.bolashak.instagramclonebackend.exception.UserException;
import com.bolashak.instagramclonebackend.model.Post;
import com.bolashak.instagramclonebackend.model.User;
import com.bolashak.instagramclonebackend.repository.PostRepository;
import com.bolashak.instagramclonebackend.service.PostService;
import com.bolashak.instagramclonebackend.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostServiceImpl implements PostService {

    Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<ApiResponseDto<?>> createPost(PostRequestDto postRequestDto, User authenticatedUser) {
        Post post = Post.builder()
                .imageUrl(postRequestDto.getImageUrl())
                .caption(postRequestDto.getCaption())
                .user(authenticatedUser)
                .createdAt(LocalDateTime.now())
                .build();
        Post savedPost = postRepository.save(post);
        PostDto createdPostDto = modelMapper.map(savedPost, PostDto.class);
        logger.info("Post: {}", createdPostDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("Post created successfully")
                                .response(createdPostDto)
                                .build()
                );

    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getAllPosts(){
        List<PostDto> postsDto = postRepository.findAllByOrderByIdDesc().stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("All posts fetched")
                                .response(postsDto)
                                .build()
                );
        }

    public ResponseEntity<ApiResponseDto<?>> getPostsByUserId(Long userId) throws UserException {
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserException(String.format("User with id %d does not exists", userId)));
        List<PostDto> posts = postRepository.findByUserOrderByIdDesc(user).stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
        logger.info("Getting posts by user id: {}", posts);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("All user's posts fetched")
                                .response(posts)
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> deletePost(Long id, User authenticatedUser)
            throws PostException {
        if (!postRepository.existsById(id)) {
            throw new PostException(String.format("Post with id %d does not exist", id));
        }

        Post postToDelete = postRepository.findPostById(id);

        if (postToDelete.getUser().equals(authenticatedUser)) {
            postRepository.delete(postToDelete);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(
                            ApiResponseDto.builder()
                                    .isSuccess(true)
                                    .message("Post deleted successfully")
                                    .build()
                    );
        }

        throw new UserException("Can't delete another user's post");
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> likePost(Long id, User authenticatedUser)
            throws PostException {
        if(!postRepository.existsById(id)){
            throw new PostException(String.format("Post with id %d does not exist", id));
        }
        Post postToLike = postRepository.findPostById(id);

        if(postToLike.getLikedByUsers().equals(authenticatedUser)){
            throw new UserException(String.format("You already liked the post with ID %d", id));
        }

        postToLike.getLikedByUsers().add(authenticatedUser);

        Post likedPost = postRepository.save(postToLike);

        PostDto postDto = modelMapper.map(likedPost, PostDto.class);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("Post liked successfully")
                                .response(postDto)
                                .build()
                );
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> unlikePost(Long id, User authenticatedUser)
    throws UserException, PostException {
        if(!postRepository.existsById(id)){
            throw new PostException(String.format("Post with id %d does not exist", id));
        }
        Post postToUnlike = postRepository.findPostById(id);
        if(!postToUnlike.getLikedByUsers().contains(authenticatedUser)){
            throw new UserException("User already unliked this post");
        }
        postToUnlike.getLikedByUsers().remove(authenticatedUser);
        Post unlikedPost = postRepository.save(postToUnlike);

        PostDto postDto = modelMapper.map(unlikedPost, PostDto.class);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("Post unliked successfully")
                                .response(postDto)
                                .build()
                );
    }

}
