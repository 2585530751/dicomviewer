package com.ncu.imagesystem.security;

import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import com.ncu.imagesystem.tools.MyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private final static String AUTH_HEADER = "Authorization";
    private final static String AUTH_HEADER_TYPE = "Bearer";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // get token from header:  Authorization: Bearer <token>
        String authHeader = request.getHeader(AUTH_HEADER);
        if (Objects.isNull(authHeader)){
            filterChain.doFilter(request,response);
            return;
        }

        // log.info("authToken:{}" , authToken);
        //verify token
        authHeader=authHeader.replace("Bearer ", "");
        try {
            new JWT(authHeader);}
        catch (Exception e){
            filterChain.doFilter(request,response);
            return;
        }
        JWT jwt = new JWT(authHeader);
        jwt.setKey(MyConstant.JWT_SIGN_KEY.getBytes(StandardCharsets.UTF_8));
        if (!(jwt.verify() && jwt.validate(0))) {
//            log.info("invalid token");
            filterChain.doFilter(request,response);
            return;
        }

        //JWTValidator.of(authToken).validateDate();

        final String content = (String) jwt.getPayload(MyConstant.TOKEN_PAYLOAD);
        JSONObject object = new JSONObject(content);
        String account = object.get("account", String.class);
        UserDetails userDetails = userDetailsService.loadUserByUsername(account);
        if(!userDetails.isEnabled()){
            filterChain.doFilter(request,response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request,response);
    }
}
