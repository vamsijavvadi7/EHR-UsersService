package com.ehr.users.service;

import com.ehr.users.dao.UserRepository;
import com.ehr.users.dto.UserDto;
import com.ehr.users.mapper.UserMapper;
import com.ehr.users.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<UserDto> createUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String hashedPassword = passwordEncoder.encode(
                userDto.getPassword() != null && !userDto.getPassword().isEmpty() ? userDto.getPassword() : "password@123"
        );
        userDto.setPassword(hashedPassword);
        try{
        User user = userMapper.toEntity(userDto);

        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(savedUser));
    }
catch (Exception e){
    return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
}
    }

    public ResponseEntity<UserDto> getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> ResponseEntity.ok(userMapper.toDto(value)))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<Object> updateUser(UserDto userDto) {
        Optional<User> existingUser = userRepository.findById(userDto.getId());

        if (existingUser.isPresent()) {
            // Check if email already exists
            if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
                return new ResponseEntity<>("Email already exists. Try a different email.", HttpStatus.CONFLICT);
            }

            User user=existingUser.get();

            if(!userDto.getEmail().isBlank()) {
                user.setEmail(userDto.getEmail());
            }

            if(!userDto.getFirstName().isBlank()) {
                user.setFirstName(userDto.getFirstName());
            }

            if(!userDto.getLastName().isBlank()) {
                user.setLastName(userDto.getLastName());
            }

            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(userMapper.toDto(updatedUser));

        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        Map<String, String> response = new HashMap<>();
        if (user.isPresent()) {
            User user1=user.get();
            user1.setIsActive(false);
            userRepository.save(user1);
            response.put("message", "User deleted successfully");
            response.put("status", "error");
            return ResponseEntity.ok(response);
        } else {
            response.put("message","Admin Not Found, consider deleted successfully" );
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }



    public Optional<UserDto> getUserByEmail(String email) {
        // Find user by email using the repository
        Optional<User> userOptional = userRepository.findByEmail(email);

        // Map the User entity to UserDto if present, or return empty if not found
        return userOptional.map(user -> userMapper.toDto(user));
    }

    public ResponseEntity<?> login(String email, String password) {
        // Retrieve user by email
        Optional<User> userOptional = userRepository.findByEmail(email);

        // Check if user exists
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return new ResponseEntity<>("Invalid password", HttpStatus.BAD_REQUEST);
        }

        if (!user.getIsActive()) {
            return new ResponseEntity<>("User Has been removed", HttpStatus.UNAUTHORIZED);
        }



        user.setLastLogin(LocalDateTime.now());
        // Map entity to DTO and return user data
        UserDto userDto = userMapper.toDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    public ResponseEntity<?> resetPassword(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();
       String encryptedpass=passwordEncoder.encode(password);
       user.setPassword(encryptedpass);
       userRepository.save(user);
       return ResponseEntity.ok("Password updated successfully");
    }

    public ResponseEntity<UserDto> getUserById(Long userid) {
        Optional<User> userOptional = userRepository.findById(userid);
        // Check if user exists
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        // Map entity to DTO and return user data
        UserDto userDto = userMapper.toDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
