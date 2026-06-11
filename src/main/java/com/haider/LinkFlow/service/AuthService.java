package com.haider.LinkFlow.service;

import com.haider.LinkFlow.dtos.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;

    public String createToken(AuthRequest authRequest) {
        try{
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    authRequest.getUsername(),
                                    authRequest.getPassword()
                            )
                    );

            String token =
                    jwtService.generateToken(authentication.getName());

            return token;
        } catch (BadCredentialsException e){
            return "Invalid username or password";
        } catch (UsernameNotFoundException e){
            return "Authentication Failed";
        }
    }
}
