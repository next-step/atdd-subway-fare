package nextstep.member;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.context.SecurityContextHolder;
import nextstep.member.domain.AnonymousUser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Component
public class AnonymousAuthenticationFilter implements Filter {
    private final String principal = AnonymousUser.PRINCIPAL;
    private final List<String> roleType = AnonymousUser.ROLES;

    private final JwtTokenProvider jwtTokenProvider;

    public AnonymousAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 스프링 시큐리티에서는 security context 내부의 Authentication에 익명사용자에 대한 authorization을 등록한다.
     * 하지만 편의를 위해 간략화 한다. -> SecurityContextHolder의 SecurityContext의 Authentication필드에 익명사용자용 토큰을 등록한다.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String authorization = httpRequest.getHeader("Authorization");

        if (!StringUtils.hasText(authorization)) {
            SecurityContextHolder securityContextHolder = new SecurityContextHolder();
            securityContextHolder.setContext(createAnonymousAuthentication());
        }

        chain.doFilter(request, response);
    }

    /**
     * 익명사용자에 대한 authorization에는 IP정보 등을 담는 것으로 보이지만, 익명사용자의 지정 principal, roleType만으로 토큰화한다.
     */
    protected String createAnonymousAuthentication() {
        return jwtTokenProvider.createToken(principal, roleType);
    }
}
