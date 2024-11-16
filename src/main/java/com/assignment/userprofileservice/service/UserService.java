package com.assignment.userprofileservice.service;

import com.assignment.userprofileservice.dto.RegisterUserRequest;
import com.assignment.userprofileservice.dto.UserResponse;
import com.assignment.userprofileservice.model.User;
import com.assignment.userprofileservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register a new user
    public UserResponse registerUser(RegisterUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setGender(request.getGender());
        user.setHeightCm(request.getHeightCm());
        user.setWeightKg(request.getWeightKg());
        user.setFitnessGoal(request.getFitnessGoal());

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    // Get user by ID
    public Optional<UserResponse> getUserById(UUID userId) {
        return userRepository.findById(userId).map(this::mapToResponse);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Map User entity to UserResponse DTO
    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setAge(user.getAge());
        response.setGender(user.getGender());
        response.setHeightCm(user.getHeightCm());
        response.setWeightKg(user.getWeightKg());
        response.setFitnessGoal(user.getFitnessGoal());
        return response;
    }
}
