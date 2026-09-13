package com.haider.LinkFlow.controller;

import com.haider.LinkFlow.dtos.request.AuthRequest;
import com.haider.LinkFlow.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login endpoint", description = "Authenticates the user and returns a JWT token in a cookie.")
    public ResponseEntity<?> auth(@Valid @RequestBody AuthRequest authRequest,
                                  HttpServletResponse response) {
        String token = authService.createToken(authRequest);
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofHours(24))
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok("Login successful");
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    @Operation(summary = "Logout endpoint", description = "Invalidates the user's JWT token.")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok("Logout successful");
    }

}
