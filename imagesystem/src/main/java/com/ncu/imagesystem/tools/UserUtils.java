package com.ncu.imagesystem.tools;

import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class UserUtils {
    public static JSONObject getUserJsonObject(){
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String authorization = request.getHeader("Authorization");
        authorization=authorization.replace("Bearer ", "");
        JWT jwt = new JWT(authorization);
        final String content = (String) jwt.getPayload(MyConstant.TOKEN_PAYLOAD);
        JSONObject object = new JSONObject(content);
        return object;
    }
}
