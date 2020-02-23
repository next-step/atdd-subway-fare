package atdd.path.configuration;

import atdd.path.auth.SignInInterceptor;
import atdd.path.auth.SignInResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final SignInInterceptor signInInterceptor;
    private final SignInResolver signInResolver;

    public WebMvcConfig(SignInInterceptor signInInterceptor, SignInResolver signInResolver) {
        this.signInInterceptor = signInInterceptor;
        this.signInResolver = signInResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signInInterceptor).addPathPatterns("/favorites/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(signInResolver);
    }
}