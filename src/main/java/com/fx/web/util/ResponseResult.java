package com.fx.web.util;

import com.fx.commons.utils.enums.ResponseStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author yc
 * @version 1.0
 * @project yjbs
 * @description TODO  RESTFUL风格统一返回体
 * @date 2020-04-03 22:21
 */
@ApiModel(value = "ResponseResult", description = "返回类型")
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 1434141678148547723L;


    @ApiModelProperty(name = "code", value = "响应状态码")
    private Integer code;

    @ApiModelProperty(name = "status", value = "响应状态,SUCCESS成功,FAIL失败")
    private ResponseStatus status;
    @ApiModelProperty(name = "data", value = "响应具体数据")
    private T data;

    public ResponseResult() {

    }

    public ResponseResult(Integer code, ResponseStatus status, T data) {
        this.code = code;
        this.status = status;
        this.data = data;

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }
}
