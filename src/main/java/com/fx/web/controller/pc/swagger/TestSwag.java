package com.fx.web.controller.pc.swagger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * @author yc
 * @version 1.0
 * @project yjbs
 * @description
 * @date 2020-04-03 22:30
 */
@ApiModel(value = "TestSwag", description = "c测试swagger实体")
public class TestSwag {


    //TODO  描述：包含API的注解都是swagger的。 其它注解为 hibernate-validator注解,结合SwaggerController里面的@valid ，@validated注解 可自行百度区别
    // 使用 validdator好处就是不用在controller 进行繁琐的校验参数，尤其还在service校验输入数据,我吐了。

    @ApiModelProperty(name = "account", value = "账号【不超过20个字符】", dataType = "String", required = true)

    @NotBlank(message = "账号不能为空")
    @Length(min = 6, max = 20, message = "账号长度只能在6-20位之间")
    private String account;

    @ApiModelProperty(name = "password", value = "密码【不超过20个字符】", dataType = "String", required = true)

    @NotNull(message = "密码不能为空")
    private String password;

    @Email(message = "不是合法的emai")
    private String email;

    @DecimalMin(value = "5", message = "年龄不能小于5岁")
    private Integer age;


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


}
