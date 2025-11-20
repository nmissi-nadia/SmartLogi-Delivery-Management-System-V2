package com.smart.utilisateur.controller;

import com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto.UserDTO;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUserDto = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }
}