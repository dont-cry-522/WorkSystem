// service/BaseUserService.java
package com.worksystem.service;

import com.worksystem.dto.request.UserLoginRequest;
import com.worksystem.dto.response.UserLoginResponse;
import com.worksystem.entity.User;

public interface BaseUserService {
    UserLoginResponse login(UserLoginRequest request);
    User getUserByUsername(String username);
}