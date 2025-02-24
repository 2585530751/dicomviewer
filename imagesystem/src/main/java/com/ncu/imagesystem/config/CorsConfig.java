package com.ncu.imagesystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.io.File;

@Configuration
class CorsConfig implements WebMvcConfigurer {
    // 通过读取配置项获取的文件上传路径
    @Value("${web.upload-path}")
    private String basePath;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080", "http://localhost:5173","http://localhost:3000","http://43.138.144.174")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(false)
                .maxAge(3600)
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
         * 资源映射路径
         * addResourceHandler:访问映射路径
         * addResourceLocations:资源绝对路径
         */
        registry.addResourceHandler("/resources/images/**").addResourceLocations("file:"+basePath+File.separator+"images"+File.separator);
        registry.addResourceHandler("/resources/imageSeg/**").addResourceLocations("file:"+basePath+File.separator+"imageSeg"+File.separator);
        registry.addResourceHandler("/resources/lungDetect/**").addResourceLocations("file:"+basePath+File.separator+"lungDetect"+File.separator);
        registry.addResourceHandler("/resources/headIcon/**").addResourceLocations("file:"+basePath+File.separator+"headIcon"+File.separator);
        registry.addResourceHandler("/resources/markImage/**").addResourceLocations("file:"+basePath+File.separator+"markImage"+File.separator);
        registry.addResourceHandler("/resources/modelImage/**").addResourceLocations("file:"+basePath+File.separator+"modelImage"+File.separator);
        registry.addResourceHandler("/resources/toolFile/**").addResourceLocations("file:"+basePath+File.separator+"toolFile"+File.separator);
    }

}

