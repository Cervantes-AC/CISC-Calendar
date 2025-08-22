package com.cisccalendar.models;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String name;
    private String email;
    private String role; // "student", "faculty", "officer", "admin"

    public User() {
        // Required empty constructor for Firebase
    }

    public User(String id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
