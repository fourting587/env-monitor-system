package com.soap.envmonitorsystem2.dto;

public record ApiResult<T>(boolean success, T data, String message) {

    public static <T> ApiResult<T> ok(T data) {
        return new ApiResult<>(true, data, null);
    }

    public static <T> ApiResult<T> ok() {
        return new ApiResult<>(true, null, null);
    }

    public static <T> ApiResult<T> fail(String message) {
        return new ApiResult<>(false, null, message);
    }
}
