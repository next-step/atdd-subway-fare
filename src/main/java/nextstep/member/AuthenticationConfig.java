package nextstep.member;

import nextstep.member.application.TokenService;
import nextstep.member.ui.AuthenticationArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthenticationConfig implements WebMvcConfigurer {

    private final TokenService tokenService;

    public AuthenticationConfig(final TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void addArgumentResolvers(final List argumentResolvers) {
        argumentResolvers.add(createAuthenticationArgumentResolver());
    }

    @Bean
    public AuthenticationArgumentResolver createAuthenticationArgumentResolver() {
        return new AuthenticationArgumentResolver(tokenService);
    }
}
