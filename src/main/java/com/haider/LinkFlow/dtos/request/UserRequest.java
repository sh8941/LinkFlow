package com.haider.LinkFlow.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request for registering a new user")
public class UserRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30,
            message = "Username must be between 3 and 30 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+$",
            message = "Username can contain only letters, numbers, dots, underscores, and hyphens"
    )
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100,
            message = "Password must be between 8 and 100 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit"
    )
    private String password;
}