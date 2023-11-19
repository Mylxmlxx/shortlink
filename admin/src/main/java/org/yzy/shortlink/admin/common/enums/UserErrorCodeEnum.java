package org.yzy.shortlink.admin.common.enums;

import org.yzy.shortlink.admin.common.convention.errorcode.IErrorCode;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/11/19 18:41
 */
public enum UserErrorCodeEnum implements IErrorCode {
    USER_NOT_EXIST("B000200", "用户不存在"),
    USER_NOT_LOGIN("B000201", "用户未登录"),
    USER_NOT_AUTH("B000202", "用户无权限");


    UserErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;
    public final String message;

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}