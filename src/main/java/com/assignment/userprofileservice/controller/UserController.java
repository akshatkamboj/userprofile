package com.assignment.userprofileservice.controller;

import com.assignment.userprofileservice.dto.RegisterUserRequest;
import com.assignment.userprofileservice.dto.UserResponse;
import com.assignment.userprofileservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegisterUserRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/getUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/validate/{userId}")
    public ResponseEntity<Map<String, Boolean>> validateUser(@PathVariable UUID userId) {
        boolean isValid = userService.isValidUser(userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/getNutritionPlan")
    public ResponseEntity<String> getNutritionPlan(@PathVariable UUID userId) {
        String response = userService.getNutritionPlanForUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/getWorkoutPlan")
    public ResponseEntity<String> getWorkoutPlan(@PathVariable UUID userId) {
        String response = userService.getWorkoutPlanForUser(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID userId,
            @RequestBody RegisterUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/{userId}/logs")
    public ResponseEntity<Void> deleteUserTrackLogs(@PathVariable UUID userId) {
        userService.deleteUserTrackLogs(userId);
        return ResponseEntity.noContent().build();
    }

}
