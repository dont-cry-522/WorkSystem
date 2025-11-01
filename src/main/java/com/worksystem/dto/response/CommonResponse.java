package com.worksystem.dto.response;

import lombok.Data;

@Data
public class CommonResponse<T> {
    private Integer code;
    private String message;
    private T data;
    private Long timestamp;

    public static <T> CommonResponse<T> success(T data) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        response.setTimestamp(System.currentTimeMillis());
        return response;
    }
}