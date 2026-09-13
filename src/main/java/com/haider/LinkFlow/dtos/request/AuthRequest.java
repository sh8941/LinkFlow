package com.haider.LinkFlow.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request for user authentication")
public class AuthRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30,
            message = "Username must be between 3 and 30 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100,
            message = "Password must be between 8 and 100 characters")
    private String password;
}