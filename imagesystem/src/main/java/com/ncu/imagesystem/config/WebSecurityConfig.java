package com.ncu.imagesystem.config;

import com.ncu.imagesystem.security.JwtAuthenticationProvider;
import com.ncu.imagesystem.security.JwtAuthenticationTokenFilter;
import com.ncu.imagesystem.security.MyAccessDeniedHandler;
import com.ncu.imagesystem.security.MyUnauthorizedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private MyUnauthorizedHandler unauthorizedHandler;

    @Autowired
    private MyAccessDeniedHandler accessDeniedHandler;

    private static final String[] AUTH_WHITE_LIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v2/api-docs/**",
            "/swagger-resources/**"
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(){
        return new JwtAuthenticationProvider();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        //由于使用的是JWT，这里不需要csrf防护
        httpSecurity.csrf().disable()
                //基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //允许对于网站静态资源的无授权访问
                .antMatchers(HttpMethod.GET,
                        "/",
                        "/*.html"
                ).permitAll()
                //对登录注册允许匿名访问
                .antMatchers("/user/login", "/user/register", "/user/refreshToken", "/user/isAccountExisted", "/user/updatePasswordByPhoneNumber").permitAll()
                //允许swagger页面访问
                .antMatchers(AUTH_WHITE_LIST).permitAll()
                //跨域请求会先进行一次options请求
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/resources/**").permitAll()
//                .antMatchers("/**").anonymous()  //关闭所有接口的token认证，生产中去掉本行代码
                .anyRequest()// 除上面外的所有请求全部需要鉴权认证
                .authenticated();
        // 禁用缓存
        httpSecurity.headers().cacheControl();
        //使用自定义provider
        httpSecurity.authenticationProvider(jwtAuthenticationProvider());
        //添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(unauthorizedHandler);

        return httpSecurity.build();
    }
}
