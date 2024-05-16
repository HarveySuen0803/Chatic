package com.harvey.result;

import lombok.Data;

/**
 * @author harvey
 */
@Data
public class Result {
    private int code;
    private String message;
    private Object data;

    public Result() {
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success() {
        return new Result(200, "success");
    }

    public static Result success(Object data) {
        return new Result(200, "success", data);
    }

    public static Result failure() {
        return new Result(500, "fail");
    }

    public static Result failure(String message) {
        return new Result(500, message);
    }

    public static Result failure(int code, String message) {
        return new Result(code, message);
    }
}
