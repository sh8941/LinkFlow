package com.haider.LinkFlow.controller;

import com.haider.LinkFlow.dtos.reponse.UserResponse;
import com.haider.LinkFlow.dtos.request.UserRequest;
import com.haider.LinkFlow.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @ApiResponse(responseCode = "200", description = "User created successfully")
    @Operation(summary = "Create User", description = "Create a new user with the provided details.")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.addUser(userRequest);
        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponse(responseCode = "200", description = "Current user retrieved successfully")
    @Operation(summary = "Get Current User", description = "Retrieve the details of the currently authenticated user.")
    public ResponseEntity<UserResponse> getUser() {
        return ResponseEntity.ok(userService.getCurrentUserResponse());
    }

}
