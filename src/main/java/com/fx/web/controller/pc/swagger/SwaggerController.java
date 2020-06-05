package com.fx.web.controller.pc.swagger;

import com.fx.commons.utils.enums.ResponseCode;
import com.fx.commons.utils.enums.ResponseStatus;
import com.fx.web.util.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author yc
 * @version 1.0   测试Swagger 和 hibernate validator
 * @project yjbs
 * @description TODO     访问swagger文档地址：http://主机：端口/swagger-ui.html
 * 自行测试传递非法的参数能否响应成功，验证hibernate validator生效没。 全局处理异常在 com.fx.commons.aspect.GlobalExceptionHandler
 * @date 2020-04-03 22:13
 */

@RestController
@RequestMapping(value = "/swagger")
@Api(value = "/swagger", description = "集成swagger demo")
@Validated
public class SwaggerController {


    @ApiOperation(value = "测试方法", httpMethod = "POST")
    @PostMapping(produces = "application/json", consumes = "application/json", value = "/post")
    public ResponseResult testSwagger(@RequestBody @Valid TestSwag swag) {

        return new ResponseResult(ResponseCode.POST.getCode(), ResponseStatus.SUCCESS, swag);

    }

    @GetMapping(value = "/get", produces = "application/json")
    @ApiOperation(value = "通过request url获取参数形式", httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query")

    })
    public ResponseResult testSwagger2(@RequestParam String userName, @RequestParam String password) {

        return new ResponseResult(ResponseCode.GET.getCode(), ResponseStatus.SUCCESS, String.format("Hello: %s,password: %s", userName, password));
    }

    @GetMapping(value = "/get/{id}", produces = "application/json")
    @ApiOperation(value = "通过url 模版变量方式获取参数形式", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "用户id", paramType = "query")
    public ResponseResult testSwagger3(@PathVariable String id) {

        return new ResponseResult(ResponseCode.GET.getCode(), ResponseStatus.SUCCESS, String.format("Hello: %s", id));
    }

    @PutMapping
    public ResponseResult testSwagger4() {
        return null;
    }

    @DeleteMapping
    public ResponseResult testSwagger5() {
        return null;
    }
}
