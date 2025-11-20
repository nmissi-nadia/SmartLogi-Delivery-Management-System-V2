package com.smart.service;


import com.smart.common.src.main.java.com.smart.entity.User;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.repository.UserRepository;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService service;

    private User user;
    private final String USER_ID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);
        user.setUsername("johndoe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(USER_ID);
        verify(userRepository).findAll();
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        Optional<User> result = service.findById(USER_ID);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(USER_ID);
        verify(userRepository).findById(USER_ID);
    }

    @Test
    void findById_WhenUserNotExists_ShouldReturnEmpty() {
        when(userRepository.findById("unknown_id")).thenReturn(Optional.empty());

        Optional<User> result = service.findById("unknown_id");

        assertThat(result).isEmpty();
        verify(userRepository).findById("unknown_id");
    }

    @Test
    void save_ShouldSaveAndReturnUser() {
        when(userRepository.save(user)).thenReturn(user);

        User result = service.save(user);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(USER_ID);
        verify(userRepository).save(user);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        service.deleteById(USER_ID);
        verify(userRepository).deleteById(USER_ID);
    }
}