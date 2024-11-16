package com.assignment.userprofileservice.dto;
import lombok.Data;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {

    @NotBlank(message = "Username is required.")
    private String username;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @Min(value = 0, message = "Age must be a positive number.")
    private Integer age;

    private String gender;

    @Min(value = 0, message = "Height must be a positive number.")
    private Double heightCm;

    @Min(value = 0, message = "Weight must be a positive number.")
    private Double weightKg;

    private String fitnessGoal;
}
