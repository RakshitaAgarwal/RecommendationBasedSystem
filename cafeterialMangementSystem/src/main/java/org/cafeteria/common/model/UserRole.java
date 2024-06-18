package org.cafeteria.common.model;

public class UserRole {
    private int id;
    private String userRole;

    public UserRole() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public UserRole(int id, String userRole) {
        this.id = id;
        this.userRole = userRole;
    }
}
