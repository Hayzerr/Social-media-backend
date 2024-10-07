package com.bolashak.instagramclonebackend.controller;

import com.bolashak.instagramclonebackend.dto.ApiResponseDto;
import com.bolashak.instagramclonebackend.dto.SignInRequestDto;
import com.bolashak.instagramclonebackend.dto.SignUpRequestDto;
import com.bolashak.instagramclonebackend.exception.RoleNotFoundException;
import com.bolashak.instagramclonebackend.exception.UserAlreadyExistsException;
import com.bolashak.instagramclonebackend.service.AuthService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<?>> signUpUser(@RequestBody @Valid SignUpRequestDto signUpRequestDto)
            throws UserAlreadyExistsException, RoleNotFoundException {
        return authService.signUpUser(signUpRequestDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponseDto<?>> signInUser(@RequestBody @Valid SignInRequestDto signInRequestDto) {
        return authService.signInUser(signInRequestDto);
    }
}
