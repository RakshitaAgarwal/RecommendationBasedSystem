package org.cafeteria.common.model;

public class User {
    private int id;
    private int userRoleId;

    private String name;

    private String password = null;
    public User() {}

    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.userRoleId = -1;
        this.password = null;
    }

    public User(int id, String name, int userRoleId, String password) {
        this.id = id;
        this.name = name;
        this.userRoleId = userRoleId;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        this.userRoleId = userRoleId;
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