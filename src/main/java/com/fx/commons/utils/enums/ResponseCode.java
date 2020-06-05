package com.fx.commons.utils.enums;

/**
 * @author yc
 * @version 1.0
 * @project yjbs
 * @description TODO  响应码枚举
 * @date 2020-04-04 12:50
 */
public enum ResponseCode {

    GET(ResponseStatus.SUCCESS, 200),                  //get请求成功返回代码

    POST(ResponseStatus.SUCCESS, 201),                 //post请求成功返回代码

    PUT(ResponseStatus.SUCCESS, 301),                  //put请求成功返回代码

    DELETE(ResponseStatus.SUCCESS, 410),               //delete请求成功返回代码

    UNAUTHORIZED(ResponseStatus.FAIL, 403),             //未授权

    CLIENT_ERROR(ResponseStatus.FAIL, 400),            //客户端请求发生错误返回代码

    SEVER_ERROR(ResponseStatus.FAIL, 500);             //服务端发生错误返回代码

    private Integer code;                                   //响应状态码

    private ResponseStatus status;                          //响应状态

    ResponseCode(ResponseStatus status, Integer code) {
        this.status = status;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
