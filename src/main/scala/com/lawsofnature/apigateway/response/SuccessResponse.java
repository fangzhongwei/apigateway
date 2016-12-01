package com.lawsofnature.apigateway.response;

/**
 * Created by fangzhongwei on 2016/12/1.
 */
public enum SuccessResponse {

    SUCCESS(null),
    SUCCESS_REGISTER("注册成功。");

    String msg;

    SuccessResponse(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
