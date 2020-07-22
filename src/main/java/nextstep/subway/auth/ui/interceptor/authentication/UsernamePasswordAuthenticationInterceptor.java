package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.application.handler.AuthenticationFailureHandler;
import nextstep.subway.auth.application.handler.AuthenticationSuccessHandler;
import nextstep.subway.auth.application.provider.AuthenticationManager;
import nextstep.subway.auth.domain.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class UsernamePasswordAuthenticationInterceptor extends AbstractAuthenticationInterceptor {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    public UsernamePasswordAuthenticationInterceptor(AuthenticationManager authenticationManager, AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler) {
        super(authenticationManager, successHandler, failureHandler);
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String principal = paramMap.get(USERNAME_FIELD)[0];
        String credentials = paramMap.get(PASSWORD_FIELD)[0];

        return new AuthenticationToken(principal, credentials);
    }
}
