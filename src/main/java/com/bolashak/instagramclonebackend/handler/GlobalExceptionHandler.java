package com.bolashak.instagramclonebackend.handler;

import com.bolashak.instagramclonebackend.dto.ApiResponseDto;
import com.bolashak.instagramclonebackend.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<?>> MethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> errorMessage = new ArrayList<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.add(error.getDefaultMessage());
        });

        return ResponseEntity
                .badRequest()
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(false)
                                .message("Registration Failed: Please provide valid data.")
                                .response(errorMessage)
                                .build()
                );
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDto<?>> UserAlreadyExistsException(UserAlreadyExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(false)
                                .message(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = RoleNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> RoleNotFoundException(RoleNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(false)
                                .message(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = UserException.class)
    public ResponseEntity<ApiResponseDto<?>> UserException(UserException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(false)
                                .message(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = PostException.class)
    public ResponseEntity<ApiResponseDto<?>> PostException(PostException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(false)
                                .message(exception.getMessage())
                                .build()
                );
    }


    @ExceptionHandler(value = CommentException.class)
    public ResponseEntity<ApiResponseDto<?>> CommentException(CommentException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(false)
                                .message(exception.getMessage())
                                .build()
                );
    }
}
