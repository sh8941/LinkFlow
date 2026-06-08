package com.haider.LinkFlow.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UrlRequest {
    @NotBlank(message = "URL cannot be blank")
    @URL(protocol = "https", message = "Only valid HTTPS URLs are allowed")
    private String longUrl;
}
