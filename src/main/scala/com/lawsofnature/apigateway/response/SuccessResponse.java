package com.lawsofnature.apigateway.response;

/**
 * Created by fangzhongwei on 2016/12/1.
 */
public enum SuccessResponse {

    SUCCESS(null),
    SUCCESS_REGISTER("注册成功。"),
    SUCCESS_SEND_CODE("验证码已发送到您的手机上，请注意查收。");

    String msg;

    SuccessResponse(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
