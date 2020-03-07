package atdd.config;

import atdd.user.web.LoginInterceptor;
import atdd.user.web.LoginUserMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final LoginUserMethodArgumentResolver loginUserArgumentResolver;

    public WebMvcConfig(LoginInterceptor loginInterceptor, LoginUserMethodArgumentResolver loginUserArgumentResolver) {
        this.loginInterceptor = loginInterceptor;
        this.loginUserArgumentResolver = loginUserArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(
                        "/users/me",
                        "/favorites/**"
                );
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver);
    }
}