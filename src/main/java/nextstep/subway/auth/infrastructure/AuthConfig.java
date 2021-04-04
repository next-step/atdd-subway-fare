package nextstep.subway.auth.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.application.AnonymousUserDetailService;
import nextstep.subway.auth.ui.authentication.SessionAuthenticationInterceptor;
import nextstep.subway.auth.ui.authentication.TokenAuthenticationInterceptor;
import nextstep.subway.auth.ui.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.subway.auth.ui.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.authorization.TokenSecurityContextPersistenceInterceptor;
import nextstep.subway.member.application.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private CustomUserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private AnonymousUserDetailService anonymousUserDetailService;

    public AuthConfig(
        CustomUserDetailsService userDetailsService,
        JwtTokenProvider jwtTokenProvider,
        AnonymousUserDetailService anonymousUserDetailService
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.anonymousUserDetailService = anonymousUserDetailService;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(userDetailsService)).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, new ObjectMapper())).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(anonymousUserDetailService));
    }
}
