// service/TeacherService.java
package com.worksystem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.worksystem.entity.User;
import com.worksystem.entity.Position;

public interface TeacherService extends IService<User>, BaseUserService {
    // 老师特有的业务方法
    boolean publishPosition(Position position);
}