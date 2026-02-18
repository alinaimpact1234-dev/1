package com.impact.lessons.models;

public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private int role;
    private boolean enabled;

    public User(String username, String password, String email, int role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.enabled = true;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public int getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public boolean getEnabled() {
        return enabled;
    }
}
