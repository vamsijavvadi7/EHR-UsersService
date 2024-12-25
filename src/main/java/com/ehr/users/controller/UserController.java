package com.ehr.users.controller;

import com.ehr.users.dto.UserDto;
import com.ehr.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get all users
    @GetMapping("/getuserbyid/{userid}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userid) {
        return userService.getUserById(userid);
    }
    // Login Check
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email,@RequestBody Map<String, String> body) {
        String password = body.get("password");
        return userService.login(email, password);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email,@RequestBody Map<String, String> body) {
        String password = body.get("password");
        return userService.resetPassword(email, password);
    }

    // Get a user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        Optional<UserDto> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new user
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    // Update user information
    @PutMapping("/updateuser")
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto) {
        try {
             return userService.updateUser(userDto);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
