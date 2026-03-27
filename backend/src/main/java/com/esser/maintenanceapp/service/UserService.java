package com.esser.maintenanceapp.service;

import com.esser.maintenanceapp.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    void deleteUser(Long id);
}