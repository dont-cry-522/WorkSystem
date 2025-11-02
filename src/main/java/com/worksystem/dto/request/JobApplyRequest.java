package com.worksystem.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobApplyRequest {
    @NotNull(message = "岗位ID不能为空")
    private Long positionId;

    private String applyReason;
}