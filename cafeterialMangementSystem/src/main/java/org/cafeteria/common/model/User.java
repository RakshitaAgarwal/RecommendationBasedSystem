package org.cafeteria.common.model;

public class User {
    private int userId;
    private UserRole userRole;

    private String name;

    private String password;
    public User() {}

    public User(int userId, String name) {
        this.userId = userId;
        this.name = name;
        this.userRole = null;
        this.password = null;
    }

    public User(int userId, String name, UserRole userRole, String password) {
        this.userId = userId;
        this.name = name;
        this.userRole = userRole;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}