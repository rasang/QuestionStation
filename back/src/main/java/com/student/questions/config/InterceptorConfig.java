package com.student.questions.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userAuthInterceptor())
                .addPathPatterns("/user/**");
        registry.addInterceptor(adminAuthInterceptor())
                .addPathPatterns("/admin/**");
    }

    /**
     * 获得验证user权限的拦截器
     * @return user拦截器
     */
    @Bean
    public AuthInterceptor userAuthInterceptor() {
        return new AuthInterceptor("isUser");
    }

    /**
     * 获得验证admin权限的拦截器
     * @return admin拦截器
     */
    @Bean
    public AuthInterceptor adminAuthInterceptor() {
        return new AuthInterceptor("isAdmin");
    }

    /**
     * 添加跨域配置
     * @param registry
     */
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://127.0.0.1:8081")  // 前端的url地址
                .allowCredentials(true)                   // 请求时是否可以携带认证信息
                .allowedMethods("*")                      // 允许的请求方式
                .maxAge(3600);
    }
}
