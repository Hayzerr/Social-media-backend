package com.bolashak.instagramclonebackend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SignInResponseDto {
    private String token;
    private String type;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
