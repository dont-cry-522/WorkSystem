package com.worksystem.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Table(name="student")
public class Student {
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