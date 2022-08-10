package nextstep;

import nextstep.member.infra.CustomUserDetailsService;
import nextstep.auth.authentication.handler.AuthenticationFailureHandler;
import nextstep.auth.authentication.handler.AuthenticationSuccessHandler;
import nextstep.auth.authentication.handler.DefaultAuthenticationFailureHandler;
import nextstep.auth.authentication.handler.DefaultAuthenticationSuccessHandler;
import nextstep.auth.authentication.handler.LoginAuthenticationFailureHandler;
import nextstep.auth.authentication.handler.TokenAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import nextstep.auth.authentication.filter.BasicAuthenticationFilter;
import nextstep.auth.authentication.filter.BearerTokenAuthenticationFilter;
import nextstep.auth.authentication.filter.UsernamePasswordAuthenticationFilter;
import nextstep.auth.authentication.provider.AuthenticationManager;
import nextstep.auth.authentication.provider.TokenAuthenticationProvider;
import nextstep.auth.authentication.provider.UserDetailsAuthenticationProvider;
import nextstep.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.authorization.secured.SecuredAnnotationChecker;
import nextstep.auth.context.SecurityContextPersistenceFilter;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthenticationInterceptor;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    private final CustomUserDetailsService customUserDetailsService;

    public AuthConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(successHandler(), loginFailureHandler(), userDetailsAuthenticationProvider())).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationInterceptor(tokenAuthenticationSuccessHandler(), loginFailureHandler(), userDetailsAuthenticationProvider())).addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationFilter(successHandler(), failureHandler(), userDetailsAuthenticationProvider()));
        registry.addInterceptor(new BearerTokenAuthenticationFilter(successHandler(), failureHandler(), tokenAuthenticationProvider()));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    @Bean
    AuthenticationManager userDetailsAuthenticationProvider() {
        return new UserDetailsAuthenticationProvider(customUserDetailsService);
    }

    @Bean
    AuthenticationManager tokenAuthenticationProvider() {
        return new TokenAuthenticationProvider(jwtTokenProvider());
    }

    @Bean
    TokenAuthenticationSuccessHandler tokenAuthenticationSuccessHandler() {
        return new TokenAuthenticationSuccessHandler(jwtTokenProvider());
    }

    @Bean
    AuthenticationSuccessHandler successHandler() {
        return new DefaultAuthenticationSuccessHandler();
    }

    @Bean
    AuthenticationFailureHandler failureHandler() {
        return new DefaultAuthenticationFailureHandler();
    }

    @Bean
    AuthenticationFailureHandler loginFailureHandler() {
        return new LoginAuthenticationFailureHandler();
    }

    @Bean
    JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey, validityInMilliseconds);
    }

    @Bean
    SecuredAnnotationChecker securedAnnotationChecker() {
        return new SecuredAnnotationChecker();
    }
}
