package com.soap.envmonitorsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResult<T>(boolean success, String message, T data) {

    public static <T> ApiResult<T> ok(T data) {
        return new ApiResult<>(true, null, data);
    }

    public static ApiResult<Void> ok() {
        return new ApiResult<>(true, null, null);
    }

    public static <T> ApiResult<T> fail(String message) {
        return new ApiResult<>(false, message, null);
    }
}
