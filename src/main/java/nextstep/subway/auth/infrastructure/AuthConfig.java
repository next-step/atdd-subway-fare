package nextstep.subway.auth.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.application.AnonymousDetails;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.ui.authentication.SessionAuthenticationInterceptor;
import nextstep.subway.auth.ui.authentication.TokenAuthenticationInterceptor;
import nextstep.subway.auth.ui.authorization.AuthenticationPrincipalArgumentResolver;
import nextstep.subway.auth.ui.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.authorization.TokenSecurityContextPersistenceInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;
    private AnonymousDetails anonymousDetails;

    public AuthConfig(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider, AnonymousDetails anonymousDetails) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.anonymousDetails = anonymousDetails;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthenticationInterceptor(userDetailsService)).addPathPatterns("/login/session");
        registry.addInterceptor(new TokenAuthenticationInterceptor(userDetailsService, jwtTokenProvider, new ObjectMapper())).addPathPatterns("/login/token");
        registry.addInterceptor(new SessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(anonymousDetails.getAnonymous()));
    }
}
