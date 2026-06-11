package com.haider.LinkFlow.controller;

import com.haider.LinkFlow.dtos.reponse.UserResponse;
import com.haider.LinkFlow.dtos.request.UserRequest;
import com.haider.LinkFlow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.addUser(userRequest);
        return ResponseEntity.ok().body(userResponse);
    }

}
