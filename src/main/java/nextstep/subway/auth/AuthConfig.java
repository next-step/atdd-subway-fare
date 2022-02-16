package nextstep.subway.auth;

import nextstep.subway.auth.authentication.SessionAuthenticationInterceptor;
import nextstep.subway.auth.authentication.TokenAuthenticationInterceptor;
import nextstep.subway.auth.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.subway.auth.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.token.JwtTokenProvider;
import nextstep.subway.auth.userdetails.UserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    public AuthConfig(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(userDetailsService)).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider)).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }
}
