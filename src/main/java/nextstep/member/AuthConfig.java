package nextstep.member;

import lombok.RequiredArgsConstructor;
import nextstep.member.ui.AuthenticationPrincipalArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class AuthConfig implements WebMvcConfigurer {
    private final AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticationPrincipalArgumentResolver);
    }
}
