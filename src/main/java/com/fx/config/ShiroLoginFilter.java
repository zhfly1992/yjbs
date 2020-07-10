package com.fx.config;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.fx.commons.utils.tools.U;
 
public class ShiroLoginFilter extends FormAuthenticationFilter {
    /**
     * 如果isAccessAllowed返回false 则执行onAccessDenied
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (request instanceof HttpServletRequest) {
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }
    
    /**
     * 在访问controller前判断是否登录，返回json，不进行重定向
     * @param request
     * @param response
     * @return true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
    	HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    	HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    	U.write(httpServletRequest, httpServletResponse, 401, "未登录");
        return false;
    }
}
