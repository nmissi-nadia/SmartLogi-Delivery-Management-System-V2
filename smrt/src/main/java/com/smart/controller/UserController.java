package com.smart.controller;

import com.smart.entity.User;
import com.smart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    public ResponseEntity<User> getById(@PathVariable String id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public User create(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public ResponseEntity<User> update(@PathVariable String id, @RequestBody User user) {
        if (!userService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        user.setId(id); // Assure que l'ID est correct pour la mise à jour
        return ResponseEntity.ok(userService.save(user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!userService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}