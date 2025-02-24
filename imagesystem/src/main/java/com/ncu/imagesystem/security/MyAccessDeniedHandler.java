package com.ncu.imagesystem.security;

import cn.hutool.json.JSONUtil;
import com.ncu.imagesystem.tools.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 当访问接口没有权限时回调
 */

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
//        log.error("access error", accessDeniedException);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        ServletOutputStream outputStream = response.getOutputStream();
        Result<Object> failed = Result.failed("权限不足，无法进行操作！");
        outputStream.write(JSONUtil.toJsonStr(failed).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
