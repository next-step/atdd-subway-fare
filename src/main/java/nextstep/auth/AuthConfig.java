package nextstep.auth;

import nextstep.auth.application.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.application.UserDetailService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailService userDetailService;

    public AuthConfig(JwtTokenProvider jwtTokenProvider, UserDetailService userDetailService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailService = userDetailService;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(jwtTokenProvider));
        argumentResolvers.add(new PathAuthenticationPrincipalArgument(jwtTokenProvider, userDetailService));
    }
}
