package nextstep.member;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.ui.AuthenticationPrincipalArgumentResolver;
import nextstep.member.ui.OptionalAuthenticationPrincipalArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    private OptionalAuthenticationPrincipalArgumentResolver optionalAuthenticationPrincipalArgumentResolver;

    public AuthConfig(JwtTokenProvider jwtTokenProvider, AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver, OptionalAuthenticationPrincipalArgumentResolver optionalAuthenticationPrincipalArgumentResolver) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationPrincipalArgumentResolver = authenticationPrincipalArgumentResolver;
        this.optionalAuthenticationPrincipalArgumentResolver = optionalAuthenticationPrincipalArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(authenticationPrincipalArgumentResolver);
        argumentResolvers.add(optionalAuthenticationPrincipalArgumentResolver);
    }
}
