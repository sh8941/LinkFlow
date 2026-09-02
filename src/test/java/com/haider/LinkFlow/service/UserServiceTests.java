package com.haider.LinkFlow.service;

import com.haider.LinkFlow.dtos.reponse.UserResponse;
import com.haider.LinkFlow.dtos.request.UserRequest;
import com.haider.LinkFlow.entity.UserEntity;
import com.haider.LinkFlow.repo.UserRepo;
import com.haider.LinkFlow.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepo userRepo;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testAddUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("username");
        userRequest.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encoded-password");
        when(userRepo.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        UserResponse response = userService.addUser(userRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("username", response.getUsername());
        assertTrue(response.getUsername().contains("user"));
        verify(passwordEncoder).encode("password");
        verify(userRepo).save(any(UserEntity.class));
    }

    @Test
    public void testGetCurrentUserResponse() {
        UserEntity currentUser = new UserEntity();
        currentUser.setId(7L);
        currentUser.setUsername("current-user");

        when(securityUtils.getCurrentUser()).thenReturn(currentUser);

        UserResponse response = userService.getCurrentUserResponse();

        assertNotNull(response);
        assertEquals(7L, response.getId());
        assertEquals("current-user", response.getUsername());
        verify(securityUtils).getCurrentUser();
    }
}
