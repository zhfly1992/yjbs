package com.fx.commons.aspect;

import com.fx.commons.utils.enums.ResponseCode;
import com.fx.commons.utils.enums.ResponseStatus;
import com.fx.web.util.ResponseResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author yc
 * @version 1.0
 * @project yjbs
 * @description TODO  全局异常处理器，建议需要打印日志错误的自行打印
 * @date 2020-04-04 12:43
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    //TODO 处理validator校验异常
    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public ResponseResult validExceptionHandler(Exception e) throws Exception {

        BindingResult bindResult = null;

        if (e instanceof BindException) {
            bindResult = ((BindException) e).getBindingResult();
        } else if (e instanceof MethodArgumentNotValidException) {
            bindResult = ((MethodArgumentNotValidException) e).getBindingResult();
        }
        String msg;
        if (bindResult != null && bindResult.hasErrors()) {

            msg = bindResult.getAllErrors().get(0).getDefaultMessage();

            return new ResponseResult(ResponseCode.CLIENT_ERROR.getCode(), ResponseStatus.FAIL, msg);
        }

        return new ResponseResult(ResponseCode.SEVER_ERROR.getCode(), ResponseStatus.FAIL, "服务端发生未知错误");
    }
}
