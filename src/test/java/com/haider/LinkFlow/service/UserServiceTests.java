package com.haider.LinkFlow.service;


import com.haider.LinkFlow.dtos.request.UserRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @Disabled
    @Test
    public void testAddUser(){
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("username");
        userRequest.setPassword("password");
        assertNotNull(userService.addUser(userRequest));
    }

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1,2,3",
            "5,10,15",
            "20,30,50"
    })
    public void test(int a, int b, int expected){
        assertEquals(expected, a+b);
    }

}
