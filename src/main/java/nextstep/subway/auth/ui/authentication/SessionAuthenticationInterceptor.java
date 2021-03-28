package nextstep.subway.auth.ui.authentication;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.SecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionAuthenticationInterceptor extends AbstractAuthenticationInterceptor {

    public SessionAuthenticationInterceptor(UserDetailsService userDetailsService, AuthenticationConverter authenticationConverter) {
        super(userDetailsService, authenticationConverter);
    }

    @Override
    protected void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        updateSession(request, response, authentication);
    }

    private void updateSession(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
