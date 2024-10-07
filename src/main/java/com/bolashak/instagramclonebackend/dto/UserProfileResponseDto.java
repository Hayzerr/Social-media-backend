package com.bolashak.instagramclonebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponseDto {
    private Long id;
    private String username;
    private String userImage;
    private String bio;
    private List<PostDto> posts = new ArrayList<>();
    private List<UserDto> followers = new ArrayList<>();
    private List<UserDto> followings = new ArrayList<>();

}
