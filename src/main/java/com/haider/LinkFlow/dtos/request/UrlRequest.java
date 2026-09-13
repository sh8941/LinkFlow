package com.haider.LinkFlow.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Schema(description = "Request for creating a shortened URL")
public class UrlRequest {
    @NotBlank(message = "URL cannot be blank")
    @URL(protocol = "https", message = "Only valid HTTPS URLs are allowed")
    private String longUrl;
}
