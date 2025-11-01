package com.worksystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("position")
public class Position {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("position_name")
    private String positionName;

    @TableField("work_content")
    private String workContent;

    @TableField("work_time")
    private String workTime;

    @TableField("salary")
    private BigDecimal salary;

    @TableField("recruitment_status")
    private String recruitmentStatus; // "recruiting", "full", "closed"

    @TableField("teacher_id")
    private Long teacherId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}