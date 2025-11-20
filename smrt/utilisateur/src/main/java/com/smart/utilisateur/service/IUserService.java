package com.smart.utilisateur.service;

import com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto.UserDTO;

public interface IUserService {
    UserDTO createUser(UserDTO userDTO);
}