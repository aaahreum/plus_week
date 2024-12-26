package com.example.demo.config;

import com.example.demo.interceptor.AdminRoleInterceptor;
import com.example.demo.interceptor.AuthInterceptor;
import com.example.demo.interceptor.UserRoleInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    // TODO: 2. 인가에 대한 이해
    private static final String[] AUTH_REQUIRED_PATH_PATTERNS = {"/users/logout", "/items/*"};
    private static final String[] USER_ROLE_REQUIRED_PATH_PATTERNS = {"/reservations/*"};
    // ADMIN 권한 확인 경로 패턴 추가
    private static final String[] ADMIN_ROLE_REQUIRED_PATH_PATTERNS = {"/admins/*"};

    private final AuthInterceptor authInterceptor;
    private final UserRoleInterceptor userRoleInterceptor;
    // ADMIN 권한을 확인하는 인터셉터
    private final AdminRoleInterceptor adminRoleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(AUTH_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE);

        registry.addInterceptor(adminRoleInterceptor)
                .addPathPatterns(ADMIN_ROLE_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 1);

        registry.addInterceptor(userRoleInterceptor)
                .addPathPatterns(USER_ROLE_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 2);
    }

}