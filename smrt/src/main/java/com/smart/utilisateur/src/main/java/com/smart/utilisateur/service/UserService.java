package com.smart.utilisateur.src.main.java.com.smart.utilisateur.service;

import com.smart.entity.User;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAll() { return userRepository.findAll(); }
    public Optional<User> findById(String id) { return userRepository.findById(id); }

    public User save(User user) {
       return userRepository.save(user);
    }

    public void deleteById(String id) { userRepository.deleteById(id); }
}