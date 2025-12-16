// entity/PositionApplication.java
package com.worksystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("position_application")
public class PositionApplication {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("student_id")
    private Long studentId;

    @TableField("position_id")
    private Long positionId;

    @TableField("application_time")
    private LocalDateTime applicationTime;

    @TableField("application_status")
    private String applicationStatus; // "pending", "approved", "rejected"

//    @TableField(fill = FieldFill.INSERT)
//    private LocalDateTime createTime;
}