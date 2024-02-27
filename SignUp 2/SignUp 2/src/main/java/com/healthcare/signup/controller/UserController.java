package com.healthcare.signup.controller;

import com.healthcare.signup.model.Role;
import com.healthcare.signup.model.User;
import com.healthcare.signup.repository.UserRepository;
import com.healthcare.signup.service.UserService;
import com.healthcare.signup.utils.Response;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Response<User> getUserById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        if (user.getRole() != Role.ADMIN) {
            return new Response<>(null, null, HttpStatus.FORBIDDEN);
        }

        try {
            var user = userService.getUserById(id);
            return new Response<>(user, null, HttpStatus.OK);
        } catch (Exception e) {
            return new Response<>(null, List.of(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
