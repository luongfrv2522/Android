package com.example.luong.location.models;

import java.io.Serializable;

public class User implements Serializable {
    private int UserId;
    private String ConnectionId;
    private String UserName;
    private String Password;
    private String FullName;
    private String Email;
    private int UserType;
    private String Description;
    private String Created;

    public User() {
    }

    public User(int userId, String connectionId, String userName, String password, String fullName, String email, int userType, String description, String created) {
        UserId = userId;
        ConnectionId = connectionId;
        UserName = userName;
        Password = password;
        FullName = fullName;
        Email = email;
        UserType = userType;
        Description = description;
        Created = created;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getConnectionId() {
        return ConnectionId;
    }

    public void setConnectionId(String connectionId) {
        ConnectionId = connectionId.trim();
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName.trim();
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password.trim();
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName.trim();
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email.trim();
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int userType) {
        UserType = userType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description.trim();
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created.trim();
    }
}
