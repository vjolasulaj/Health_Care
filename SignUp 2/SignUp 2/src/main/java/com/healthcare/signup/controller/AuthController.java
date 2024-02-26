package com.healthcare.signup.controller;

import com.healthcare.signup.dto.LogInDto;
import com.healthcare.signup.dto.LoginResponse;
import com.healthcare.signup.dto.SignUpDTO;
import com.healthcare.signup.model.User;
import com.healthcare.signup.service.AuthService;
import com.healthcare.signup.service.UserService;
import com.healthcare.signup.utils.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public Response<User> signup(@RequestBody SignUpDTO signUpDTO) {
        try {
            var user = authService.signup(signUpDTO);

            return new Response<>(user, null, HttpStatus.OK);
        } catch (Exception e) {
            return new Response<>(null, List.of(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public Response<LoginResponse> login(@RequestBody LogInDto logInDto) {
        try {
            var user = authService.login(logInDto);

            return new Response<LoginResponse>(user, null, HttpStatus.OK);
        } catch (Exception e) {
            return new Response<LoginResponse>(null, List.of(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/me")
    public Response<User> me(@AuthenticationPrincipal User user) {
        try {
            var u = userService.getUserByEmail(user.getEmail());
            return new Response<User>(u, null, HttpStatus.OK);
        } catch (Exception e) {
            return new Response<User>(null, List.of(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
