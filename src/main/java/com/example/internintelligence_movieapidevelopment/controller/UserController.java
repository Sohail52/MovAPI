package com.example.internintelligence_movieapidevelopment.controller;

import com.example.internintelligence_movieapidevelopment.dto.request.ChangePasswordDto;
import com.example.internintelligence_movieapidevelopment.dto.response.UserResponseDto;
import com.example.internintelligence_movieapidevelopment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PatchMapping("/change-password")
    public void changePassword(@Validated @RequestBody ChangePasswordDto changePassword) {
        userService.changePassword(changePassword);
    }

    @DeleteMapping("/delete")
    public void deleteOwnProfile() {
        userService.deleteOwnProfile();
    }

    //for admin
    @GetMapping("/admin/get-all")
    public List<UserResponseDto> getUsers() {
        return userService.getUsers();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/delete/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
    }

}
