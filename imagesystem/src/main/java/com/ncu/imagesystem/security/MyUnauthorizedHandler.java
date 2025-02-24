package com.ncu.imagesystem.security;

import cn.hutool.json.JSONUtil;
import com.ncu.imagesystem.tools.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 未认证时回调，也就是说没有登录
 */

@Component
public class MyUnauthorizedHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        log.error("Unauthorized error", authException);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        ServletOutputStream outputStream = response.getOutputStream();
        Result<Object> failed = Result.failedToken("认证失败，请重新登录。");
        outputStream.write(JSONUtil.toJsonStr(failed).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
