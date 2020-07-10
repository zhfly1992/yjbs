package com.fx.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author yc
 * @version 1.0
 * @project yjbs
 * 访问swagger文档地址：http://主机：端口/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(basePackage(getCtls()))
                .paths(PathSelectors.any())
                .build();
    }

    private String getCtls() {
    	String ctlstr = "";
    	
    	List<String> ctls = new ArrayList<String>();
    	
    	// 电脑端-单位管理
    	ctls.add("com.fx.web.controller.pc.company");
    	
    	// 电脑端-个人管理-用户模块
    	ctls.add("com.fx.web.controller.pc.customer.cus");
    	
    	// 电脑端-个人管理-订单模块
    	ctls.add("com.fx.web.controller.pc.customer.order");
    	
    	// 移动端-客户、驾驶员、业务员、计调
//    	ctls.add("com.fx.web.controller.mobile");
    	
    	// 公共接口模块
    	ctls.add("com.fx.web.controller.common");
    	
    	// 公共接口模块
    	ctls.add("com.fx.entity");
    	
    	if(ctls.size() > 0) ctlstr = StringUtils.join(ctls.toArray(), ";");
    	
    	return ctlstr;
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("遇见巴士（yjbs）接口文档")
                .description("描述：电脑端/移动端服务")
                .termsOfServiceUrl("访问地址：http://121.36.52.250/fx")
                .contact(new Contact("遇见巴士", "http://121.36.52.250/fx", ""))
                .version("版本号: v1.0")
                .build();
    }

    public static Predicate<RequestHandler> basePackage(final String basePackage) {
        return input -> declaringClass(input).transform(handlerPackage(basePackage)).or(true);
    }
    
    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage)     {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackage.split(";")) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    @SuppressWarnings("deprecation")
	private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }
    
}
