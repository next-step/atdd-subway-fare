package nextstep;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import support.auth.authentication.filter.BasicAuthenticationFilter;
import support.auth.authentication.filter.BearerTokenAuthenticationFilter;
import support.auth.authentication.filter.UsernamePasswordAuthenticationFilter;
import support.auth.authentication.handler.*;
import support.auth.authentication.provider.AuthenticationManager;
import support.auth.authentication.provider.TokenAuthenticationProvider;
import support.auth.authentication.provider.UserDetailsAuthenticationProvider;
import support.auth.authorization.AuthenticationPrincipalArgumentResolver;
import support.auth.authorization.secured.SecuredAnnotationChecker;
import support.auth.context.SecurityContextPersistenceFilter;
import support.auth.token.JwtTokenProvider;
import support.auth.token.TokenAuthenticationInterceptor;
import support.ticket.TicketArgumentResolver;

import java.util.List;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;
    private final CustomUserDetailsService customUserDetailsService;
    private final MemberRepository memberRepository;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityContextPersistenceFilter());
        registry.addInterceptor(new UsernamePasswordAuthenticationFilter(successHandler(), loginFailureHandler(), userDetailsAuthenticationProvider())).addPathPatterns("/login/form");
        registry.addInterceptor(new TokenAuthenticationInterceptor(tokenAuthenticationSuccessHandler(), loginFailureHandler(), userDetailsAuthenticationProvider())).addPathPatterns("/login/token");
        registry.addInterceptor(new BasicAuthenticationFilter(successHandler(), failureHandler(), userDetailsAuthenticationProvider()));
        registry.addInterceptor(new BearerTokenAuthenticationFilter(successHandler(), failureHandler(), tokenAuthenticationProvider()));
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
        argumentResolvers.add(new TicketArgumentResolver(memberRepository));
    }

    @Bean
    AuthenticationManager userDetailsAuthenticationProvider() {
        return new UserDetailsAuthenticationProvider(customUserDetailsService);
    }

    @Bean
    AuthenticationManager tokenAuthenticationProvider() {
        return new TokenAuthenticationProvider(jwtTokenProvider());
    }

    @Bean
    TokenAuthenticationSuccessHandler tokenAuthenticationSuccessHandler() {
        return new TokenAuthenticationSuccessHandler(jwtTokenProvider());
    }

    @Bean
    AuthenticationSuccessHandler successHandler() {
        return new DefaultAuthenticationSuccessHandler();
    }

    @Bean
    AuthenticationFailureHandler failureHandler() {
        return new DefaultAuthenticationFailureHandler();
    }

    @Bean
    AuthenticationFailureHandler loginFailureHandler() {
        return new LoginAuthenticationFailureHandler();
    }

    @Bean
    JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey, validityInMilliseconds);
    }

    @Bean
    SecuredAnnotationChecker securedAnnotationChecker() {
        return new SecuredAnnotationChecker();
    }
}
