package nextstep.auth;

import nextstep.auth.principal.AuthenticationPrincipalArgumentResolver;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.userdetails.UserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    public AuthConfig(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(jwtTokenProvider, userDetailsService));
    }
}
