// dto/response/CommonResponse.java
package com.worksystem.dto.response;

import lombok.Data;

@Data
public class CommonResponse<T> {
    private Integer code;
    private String message;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    private T data;
    private Long timestamp;

    public CommonResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> CommonResponse<T> success(T data) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    public static <T> CommonResponse<T> success(String message, T data) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> CommonResponse<T> error(Integer code, String message) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}