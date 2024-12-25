package com.ehr.users.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for each user

    @Column(nullable = false, unique = true)
    private String email;  // Email used for login (indexed for fast lookup)

    @Column(nullable = false)
    private String password;  // Encrypted password for login

    @Column(nullable = false)
    private String role;  // User role (doctor, staff, patient)

    private String firstName;  // User's first name

    private String lastName;  // User's last name

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;  // Timestamp for when the user was created

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;  // Timestamp for updates

    private LocalDateTime lastLogin;  // Timestamp for the last login

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;  // Indicates if the user is active or disabled

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
