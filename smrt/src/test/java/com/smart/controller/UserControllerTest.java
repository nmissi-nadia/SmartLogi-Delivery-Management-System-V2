package com.smart.controller;


import com.smart.common.src.main.java.com.smart.Enum.Role;
import com.smart.common.src.main.java.com.smart.entity.User;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.controller.UserController;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("user1");
        user.setUsername("johndoe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setRole(Role.DESTINATAIRE);
    }

    @Test
    void getAll_ShouldReturnAllUsers() {
        // Arrange
        when(userService.findAll()).thenReturn(Arrays.asList(user));

        // Act
        List<User> result = userController.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getId());
        verify(userService).findAll();
    }

    @Test
    void getById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userService.findById("user1")).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<User> response = userController.getById("user1");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("user1", response.getBody().getId());
        verify(userService).findById("user1");
    }

    @Test
    void getById_WhenUserNotExists_ShouldReturnNotFound() {
        // Arrange
        when(userService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = userController.getById("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService).findById("nonexistent");
    }

    @Test
    void create_ShouldReturnCreatedUser() {
        // Arrange
        when(userService.save(any(User.class))).thenReturn(user);

        // Act
        User result = userController.create(user);

        // Assert
        assertNotNull(result);
        assertEquals("user1", result.getId());
        verify(userService).save(any(User.class));
    }

    @Test
    void update_WhenUserExists_ShouldReturnUpdatedUser() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setId("user1");
        updatedUser.setUsername("updatedUser");
        updatedUser.setEmail("updated@example.com");

        when(userService.findById("user1")).thenReturn(Optional.of(user));
        when(userService.save(any(User.class))).thenReturn(updatedUser);

        // Act
        ResponseEntity<User> response = userController.update("user1", updatedUser);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("updatedUser", response.getBody().getUsername());
        verify(userService).findById("user1");
        verify(userService).save(any(User.class));
    }

    @Test
    void update_WhenUserNotExists_ShouldReturnNotFound() {
        // Arrange
        when(userService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = userController.update("nonexistent", user);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService).findById("nonexistent");
        verify(userService, never()).save(any(User.class));
    }

    @Test
    void delete_WhenUserExists_ShouldReturnNoContent() {
        // Arrange
        when(userService.findById("user1")).thenReturn(Optional.of(user));
        doNothing().when(userService).deleteById("user1");

        // Act
        ResponseEntity<Void> response = userController.delete("user1");

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).findById("user1");
        verify(userService).deleteById("user1");
    }

    @Test
    void delete_WhenUserNotExists_ShouldReturnNotFound() {
        // Arrange
        when(userService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> response = userController.delete("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService).findById("nonexistent");
        verify(userService, never()).deleteById(anyString());
    }
}