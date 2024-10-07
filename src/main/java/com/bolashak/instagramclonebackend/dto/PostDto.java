package com.bolashak.instagramclonebackend.dto;

import com.bolashak.instagramclonebackend.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String imageUrl;
    private String caption;
    private LocalDateTime createdAt;
    private UserDto user;
    private List<CommentDto> comments = new ArrayList<>();
    private List<UserDto> likedByUsers = new ArrayList<>();
}
