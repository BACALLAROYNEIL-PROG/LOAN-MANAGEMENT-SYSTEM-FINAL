package com.cooperative.loan_management_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
// 1. Rename the table to "users" to avoid H2's reserved keyword "USER"
@Table(name = "users") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String role; // ADMIN, STAFF

    // 2. REMOVED the isEmpty() method. 
    // It is not needed because we fixed the check in SecurityConfig.java 
    // to use 'existingUser == null'.
}