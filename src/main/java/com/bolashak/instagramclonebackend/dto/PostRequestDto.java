package com.bolashak.instagramclonebackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostRequestDto {

    @NotBlank(message = "Image is required")
    private String imageUrl;

    @Size(max = 255, message = "Length of caption can be at most 255 characters")
    private String caption;

}
