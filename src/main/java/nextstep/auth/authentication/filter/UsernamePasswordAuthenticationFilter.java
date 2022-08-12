package nextstep.auth.authentication.filter;

import nextstep.auth.authentication.handler.AuthenticationSuccessHandler;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.handler.AuthenticationFailureHandler;
import nextstep.auth.authentication.provider.AuthenticationManager;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationFilter {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    public UsernamePasswordAuthenticationFilter(AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler, AuthenticationManager authenticationManager) {
        super(successHandler, failureHandler, authenticationManager);
    }

    @Override
    protected AuthenticationToken convert(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String username = paramMap.get(USERNAME_FIELD)[0];
        String password = paramMap.get(PASSWORD_FIELD)[0];

        return new AuthenticationToken(username, password);
    }

    @Override
    protected boolean shouldContinueChainAfterSuccessfulAuthentication() {
        return false;
    }

    @Override
    protected boolean shouldContinueChainAfterUnSuccessfulAuthentication() {
        return false;
    }
}
