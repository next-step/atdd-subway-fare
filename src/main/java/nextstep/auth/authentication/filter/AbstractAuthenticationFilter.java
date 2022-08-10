package nextstep.auth.authentication.filter;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.handler.AuthenticationFailureHandler;
import nextstep.auth.authentication.handler.AuthenticationSuccessHandler;
import nextstep.auth.authentication.provider.AuthenticationManager;
import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractAuthenticationFilter implements HandlerInterceptor {
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final AuthenticationManager authenticationManager;

    protected AbstractAuthenticationFilter(AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler, AuthenticationManager authenticationManager) {
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            Authentication authentication = attemptAuthentication(request);
            successHandler.onAuthenticationSuccess(request, response, authentication);
            return shouldContinueChainAfterSuccessfulAuthentication();
        } catch (Exception e) {
            failureHandler.onAuthenticationFailure(request, response, e);
            return shouldContinueChainAfterUnSuccessfulAuthentication();
        }
    }

    protected Authentication attemptAuthentication(HttpServletRequest request) throws IOException {
        AuthenticationToken token = convert(request);
        return authenticationManager.authenticate(token);
    }

    protected abstract AuthenticationToken convert(HttpServletRequest request) throws IOException;

    protected abstract boolean shouldContinueChainAfterSuccessfulAuthentication();

    protected abstract boolean shouldContinueChainAfterUnSuccessfulAuthentication();
}
