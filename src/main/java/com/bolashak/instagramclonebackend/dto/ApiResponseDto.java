package com.bolashak.instagramclonebackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponseDto<T> {
    private boolean isSuccess;
    private String message;
    private T response;

    public ApiResponseDto(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public ApiResponseDto(boolean isSuccess, String message, T response) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.response = response;
    }
}
