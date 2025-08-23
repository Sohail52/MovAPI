package com.example.internintelligence_movieapidevelopment.service;

import com.example.internintelligence_movieapidevelopment.dao.entity.User;
import com.example.internintelligence_movieapidevelopment.dao.repository.UserRepository;
import com.example.internintelligence_movieapidevelopment.dto.request.ChangePasswordDto;
import com.example.internintelligence_movieapidevelopment.dto.response.UserResponseDto;
import com.example.internintelligence_movieapidevelopment.exception.IllegalArgumentException;
import com.example.internintelligence_movieapidevelopment.exception.ResourceNotFoundException;
import com.example.internintelligence_movieapidevelopment.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto getUser(Integer id) {
        log.info("Attempting get user with {} ID", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("User with {} id is not found", id);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        return userMapper.toDto(user);
    }


    public void changePassword(ChangePasswordDto changePassword) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Operation of changing password for user {} started...", currentUser);
        User user = userRepository.findByUsername(currentUser).orElseThrow(() -> {
            log.warn("User {} doesn't exist", currentUser);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        if (changePassword.getNewPassword().equals(changePassword.getRetryPassword()) && passwordEncoder.matches(changePassword.getCurrentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
            userRepository.save(user);
            log.info("User's password is successfully changed");
        } else {
            log.error("Failed to change password");
            throw new IllegalArgumentException("Old password entered incorrectly or new passwords do not match");
        }
    }


    public void deleteOwnProfile() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Deleting process of user {} started...", currentUser);
        User user = userRepository.findByUsername(currentUser).orElseThrow(() -> {
            log.warn("Failed to delete user: user {} doesn't exist", currentUser);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        userRepository.delete(user);
        log.info("User successfully deleted.");
    }


    //for admin
    public List<UserResponseDto> getUsers() {
        log.info("Attempting get all users...");
        List<User> users = userRepository.findAll();
        log.info("All users returned.");
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    public void deleteUser(Integer userId) {
        log.info("Deleting process of user {} started by admin", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Failed to delete user: user {} doesn't exist", userId);
            return new ResourceNotFoundException("USER_NOT_FOUND");
        });
        userRepository.delete(user);
        log.info("User successfully deleted.");
    }


}
