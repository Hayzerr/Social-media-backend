package com.bolashak.instagramclonebackend.service;

import com.bolashak.instagramclonebackend.dto.ApiResponseDto;
import com.bolashak.instagramclonebackend.dto.SignInRequestDto;
import com.bolashak.instagramclonebackend.dto.SignUpRequestDto;
import com.bolashak.instagramclonebackend.exception.RoleNotFoundException;
import com.bolashak.instagramclonebackend.exception.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ResponseEntity<ApiResponseDto<?>> signUpUser(SignUpRequestDto signUpRequestDto)
            throws UserAlreadyExistsException, RoleNotFoundException;
    ResponseEntity<ApiResponseDto<?>> signInUser(SignInRequestDto signInRequestDto);
}
