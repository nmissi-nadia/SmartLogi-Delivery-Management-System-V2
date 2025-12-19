package com.smart.service;

import com.smart.entity.User;
import com.smart.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() { return userRepository.findAll(); }
    public Optional<User> findById(String id) { return userRepository.findById(id); }

    public User save(User user) {
        User savedUser = userRepository.save(user);
        log.info("User saved: {}", savedUser.getUsername());
        return savedUser;
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
        log.info("User deleted: {}", id);
    }
}