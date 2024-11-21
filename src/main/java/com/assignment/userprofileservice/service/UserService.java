package com.assignment.userprofileservice.service;

import com.assignment.userprofileservice.dto.RegisterUserRequest;
import com.assignment.userprofileservice.dto.UserResponse;
import com.assignment.userprofileservice.model.User;
import com.assignment.userprofileservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {
    @Autowired
    private RestTemplate restTemplate;
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

    public boolean isValidUser(UUID userId) {
        return userRepository.existsById(userId);
    }

    public String getNutritionPlanForUser(UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        String fitnessGoal = userOptional.get().getFitnessGoal();
        String url = "https://44ce-2402-e280-3e64-84d-86d3-5942-8956-4c57.ngrok-free.app/api/meal-plans/goals?goals=" + fitnessGoal;
        return restTemplate.getForObject(url, String.class);
    }

    public String getWorkoutPlanForUser(UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        String fitnessGoal = userOptional.get().getFitnessGoal();
        String url = "https://fast-vans-smash.loca.lt/api/plans/" + fitnessGoal;
        return restTemplate.getForObject(url, String.class);
    }

    public void deleteUser(UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(userId);
    }

    public UserResponse updateUser(UUID userId, RegisterUserRequest request) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();
        // Update user details
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setGender(request.getGender());
        user.setHeightCm(request.getHeightCm());
        user.setWeightKg(request.getWeightKg());
        user.setFitnessGoal(request.getFitnessGoal());
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        return mapToResponse(updatedUser);
    }

    public String getUserTrackLogs(UUID userId) {

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        String fitnessGoal = userOptional.get().getFitnessGoal();
        String url = "https://6628-2401-4900-1cb8-3e2-e872-4ecf-b3e5-2c8.ngrok-free.app/api/progress/summary/" + userId;
        return restTemplate.getForObject(url, String.class);
    }

    public void deleteUserTrackLogs(UUID userId) {
        String url = "https://6628-2401-4900-1cb8-3e2-e872-4ecf-b3e5-2c8.ngrok-free.app/api/progress/user/" + userId + "/logs";

        try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("User track logs deleted successfully.");
            } else {
                throw new RuntimeException("Failed to delete user track logs, HTTP status: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error while deleting user track logs: " + e.getMessage());
        }
    }


}
