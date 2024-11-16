package com.assignment.userprofileservice.dto;
import lombok.Data;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID userId;
    private String username;
    private String email;
    private Integer age;
    private String gender;
    private Double heightCm;
    private Double weightKg;
    private String fitnessGoal;
}
