package nextstep.config;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.ui.AuthenticationPrincipalArgumentResolver;
import nextstep.subway.ui.PathSearchConditionConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    public WebConfig(JwtTokenProvider jwtTokenProvider, AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationPrincipalArgumentResolver = authenticationPrincipalArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(authenticationPrincipalArgumentResolver);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new PathSearchConditionConverter());
    }
}
