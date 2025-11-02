// service/StudentService.java
package com.worksystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.worksystem.entity.User;

public interface StudentService extends IService<User>, BaseUserService {
    // 学生特有的业务方法
    boolean applyPosition(Long studentId, Long positionId);
}