package com.example.internintelligence_movieapidevelopment.controller;

import com.example.internintelligence_movieapidevelopment.dao.entity.Authority;
import com.example.internintelligence_movieapidevelopment.dao.entity.User;
import com.example.internintelligence_movieapidevelopment.dao.repository.UserRepository;
import com.example.internintelligence_movieapidevelopment.dto.request.UserRequestDto;
import com.example.internintelligence_movieapidevelopment.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            Map<String, Object> body = new HashMap<>();
            body.put("token", token);
            body.put("username", userDetails.getUsername());
            return ResponseEntity.ok(body);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequestDto dto) {
        try {
            System.out.println("=== REGISTRATION ATTEMPT ===");
            System.out.println("Username: " + dto.getUserName());
            System.out.println("Email: " + dto.getEmail());
            System.out.println("Password length: " + (dto.getPassword() != null ? dto.getPassword().length() : "null"));
            System.out.println("Confirm password length: " + (dto.getConfirmPassword() != null ? dto.getConfirmPassword().length() : "null"));
            System.out.println("Password matches confirm: " + dto.getPassword().equals(dto.getConfirmPassword()));
            
            // Test database connection
            try {
                long userCount = userRepository.count();
                System.out.println("Database connection successful. Current user count: " + userCount);
            } catch (Exception dbEx) {
                System.err.println("Database connection failed: " + dbEx.getMessage());
                return ResponseEntity.status(500).body(Map.of("message", "Database connection failed: " + dbEx.getMessage()));
            }
            
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                System.out.println("Password mismatch for user: " + dto.getUserName());
                return ResponseEntity.badRequest().body(Map.of("message", "Passwords do not match"));
            }
            if (userRepository.existsByUsername(dto.getUserName())) {
                System.out.println("Username already taken: " + dto.getUserName());
                return ResponseEntity.badRequest().body(Map.of("message", "Username already taken"));
            }
            
            User user = new User();
            user.setUsername(dto.getUserName());
            user.setEmail(dto.getEmail());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));

            Authority userAuthority = new Authority();
            userAuthority.setName("USER");
            userAuthority.setUser(user);
            user.setAuthorities(List.of(userAuthority));

            User savedUser = userRepository.save(user);
            System.out.println("User saved successfully with ID: " + savedUser.getId());

            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(Map.of("token", token, "username", user.getUsername()));
        } catch (Exception e) {
            System.err.println("Error during registration: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Registration failed: " + e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "OK", "message", "Auth service is running"));
    }
}


