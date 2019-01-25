package com.example.luong.location.entities;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private int UserId;
    private int DeviceId;
    private String UserName;
    private String Password;
    private String FullName;
    private String Email;
    private int UserType;
    private String Description;
    private Date Created;

    public User(int userId, int deviceId, String userName, String password, String fullName, String email, int userType, String description, Date created) {
        UserId = userId;
        DeviceId = deviceId;
        UserName = userName.trim();
        Password = password.trim();
        FullName = fullName.trim();
        Email = email.trim();
        UserType = userType;
        Description = description.trim();
        Created = created;
    }

    public User() {}

    public int getUserId() {
        return UserId;
    }

    public int getDeviceId() {
        return DeviceId;
    }

    public String getUserName() {
        return UserName;
    }

    public String getPassword() {
        return Password;
    }

    public String getFullName() {
        return FullName;
    }

    public String getEmail() {
        return Email;
    }

    public int getUserType() {
        return UserType;
    }

    public String getDescription() {
        return Description;
    }

    public Date getCreated() {
        return Created;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public void setDeviceId(int deviceId) {
        DeviceId = deviceId;
    }

    public void setUserName(String userName) {
        UserName = userName.trim();
    }

    public void setPassword(String password) {
        Password = password.trim();
    }

    public void setFullName(String fullName) {
        FullName = fullName.trim();
    }

    public void setEmail(String email) {
        Email = email.trim();
    }

    public void setUserType(int userType) {
        UserType = userType;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setCreated(Date created) {
        Created = created;
    }
}
