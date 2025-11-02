// dto/response/UserLoginResponse.java
package com.worksystem.dto.response;

import lombok.Data;
import org.apache.catalina.User;

@Data
public class UserLoginResponse {
    private Long userId;
    private String username;
    private String name;
    private String userType; // "student" 或 "teacher"
    private String college;
    private User user;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String token;
    public UserLoginResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }

}