package com.haider.LinkFlow.controller;

import com.haider.LinkFlow.dtos.request.AuthRequest;
import com.haider.LinkFlow.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<?> auth(@RequestBody AuthRequest authRequest) {
        String token = authService.createToken(authRequest);
        return ResponseEntity.ok(token);
    }
}
