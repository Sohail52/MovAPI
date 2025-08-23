package com.example.internintelligence_movieapidevelopment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$"
            , message = "Invalid Password: Must be at least 8 characters long, with at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    private String newPassword;

    @NotBlank(message = "Retry password is required")
    private String retryPassword;
}
