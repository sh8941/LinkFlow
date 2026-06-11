package com.haider.LinkFlow.config;

import com.haider.LinkFlow.entity.UserEntity;
import com.haider.LinkFlow.exception.ResourceNotFound;
import com.haider.LinkFlow.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    @Autowired
    private UserRepo userRepo;

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFound("User not found"));
    }
}
