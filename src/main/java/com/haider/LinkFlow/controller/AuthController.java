package com.haider.LinkFlow.controller;

import com.haider.LinkFlow.dtos.request.AuthRequest;
import com.haider.LinkFlow.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> auth(@Valid @RequestBody AuthRequest authRequest) {
    System.out.println("Auth Request: " + authRequest);
        String token = authService.createToken(authRequest);
        return ResponseEntity.ok(Map.of(
                "token", token
        ));
    }
}
