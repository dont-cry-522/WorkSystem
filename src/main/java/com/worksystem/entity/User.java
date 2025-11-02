// entity/User.java
package com.worksystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("name")
    private String name;

    @TableField("user_type")
    private String userType; // "student" 或 "teacher"

    @TableField("college")
    private String college;

    @TableField("mobile")
    private String mobile;

    @TableField("email")
    private String email;

    @TableField("introduction")
    private String introduction;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}