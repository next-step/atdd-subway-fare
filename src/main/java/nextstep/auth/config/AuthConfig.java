package nextstep.auth.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nextstep.auth.principal.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.validation.AuthorizationValidatorGroup;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorizationValidatorGroup authorizationValidatorGroup;

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(jwtTokenProvider, authorizationValidatorGroup));
    }
}
