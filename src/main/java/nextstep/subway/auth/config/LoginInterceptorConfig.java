package nextstep.subway.auth.config;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.application.handler.IssueTokenSuccessHandler;
import nextstep.subway.auth.application.handler.SaveSessionSuccessHandler;
import nextstep.subway.auth.application.handler.SimpleUrlAuthenticationFailureHandler;
import nextstep.subway.auth.application.provider.AuthenticationProvider;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.ui.interceptor.authentication.TokenAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.authentication.UsernamePasswordAuthenticationInterceptor;
import nextstep.subway.auth.ui.interceptor.authorization.SessionSecurityContextPersistenceInterceptor;
import nextstep.subway.auth.ui.interceptor.authorization.TokenSecurityContextPersistenceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("!test")
public class LoginInterceptorConfig implements WebMvcConfigurer {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailsService userDetailsService;

    public LoginInterceptorConfig(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(createUsernamePasswordAuthenticationInterceptor()).addPathPatterns("/login/session");
        registry.addInterceptor(createTokenAuthenticationInterceptor()).addPathPatterns("/login/token");
        registry.addInterceptor(createSessionSecurityContextPersistenceInterceptor());
        registry.addInterceptor(createTokenSecurityContextPersistenceInterceptor());
    }

    @Bean
    public UsernamePasswordAuthenticationInterceptor createUsernamePasswordAuthenticationInterceptor() {
        return new UsernamePasswordAuthenticationInterceptor(
                createAuthenticationProvider(),
                createSaveSessionSuccessHandler(),
                createSimpleUrlAuthenticationFailureHandler()
        );
    }

    @Bean
    public TokenAuthenticationInterceptor createTokenAuthenticationInterceptor() {
        return new TokenAuthenticationInterceptor(
                createAuthenticationProvider(),
                createIssueTokenSuccessHandler(),
                createSimpleUrlAuthenticationFailureHandler()
        );
    }

    @Bean
    public AuthenticationProvider createAuthenticationProvider() {
        return new AuthenticationProvider(userDetailsService);
    }

    @Bean
    public IssueTokenSuccessHandler createIssueTokenSuccessHandler() {
        return new IssueTokenSuccessHandler(jwtTokenProvider);
    }

    @Bean
    public SaveSessionSuccessHandler createSaveSessionSuccessHandler() {
        return new SaveSessionSuccessHandler();
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler createSimpleUrlAuthenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler();
    }

    @Bean
    public SessionSecurityContextPersistenceInterceptor createSessionSecurityContextPersistenceInterceptor() {
        return new SessionSecurityContextPersistenceInterceptor();
    }

    @Bean
    public TokenSecurityContextPersistenceInterceptor createTokenSecurityContextPersistenceInterceptor() {
        return new TokenSecurityContextPersistenceInterceptor(jwtTokenProvider);
    }
}
