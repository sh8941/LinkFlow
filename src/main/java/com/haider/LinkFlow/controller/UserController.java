package com.haider.LinkFlow.controller;

import com.haider.LinkFlow.dtos.reponse.UserResponse;
import com.haider.LinkFlow.dtos.request.UserRequest;
import com.haider.LinkFlow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.addUser(userRequest);
        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUser() {
        return ResponseEntity.ok(userService.getCurrentUserResponse());
    }

}
