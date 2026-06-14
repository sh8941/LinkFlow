package com.haider.LinkFlow.service;

import com.haider.LinkFlow.utils.SecurityUtils;
import com.haider.LinkFlow.dtos.reponse.UserResponse;
import com.haider.LinkFlow.dtos.request.UserRequest;
import com.haider.LinkFlow.entity.UserEntity;
import com.haider.LinkFlow.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse addUser(UserRequest userRequest) {
        // request to entity
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRequest.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userEntity.setActive(true);

        // saving
        UserEntity saved = userRepo.save(userEntity);

        // saved to response
        UserResponse userResponse = new UserResponse();
        userResponse.setId(saved.getId());
        userResponse.setUsername(saved.getUsername());
        return userResponse;
    }

    public UserResponse getCurrentUserResponse() {
        UserEntity currentUser = securityUtils.getCurrentUser();
        UserResponse userResponse = new UserResponse();
        userResponse.setId(currentUser.getId());
        userResponse.setUsername(currentUser.getUsername());
        return userResponse;
    }
}
