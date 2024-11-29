package iuh.fit.se.techgalaxy.frontend.admin.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebMvcConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**") // Áp dụng cho mọi đường dẫn
                .excludePathPatterns(
                        "/login",           // Loại trừ login
                        "/css/**",          // Loại trừ các file trong thư mục css
                        "/js/**",           // Loại trừ các file trong thư mục js
                        "/images/**",       // Loại trừ các file trong thư mục images
                        "/bootstrap.js/**", // Loại trừ thư mục bootstrap.js
                        "/chart.js/**",     // Loại trừ thư mục chart.js
                        "/datatables/**",   // Loại trừ thư mục datatables
                        "/fontawesome-free/**", // Loại trừ thư mục fontawesome-free
                        "/img/**",          // Loại trừ thư mục img
                        "/jquery/**",       // Loại trừ thư mục jquery
                        "/jquery-easing/**" // Loại trừ thư mục jquery-easing
                );
    }
}
