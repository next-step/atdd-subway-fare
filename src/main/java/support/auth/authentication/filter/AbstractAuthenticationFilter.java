package support.auth.authentication.filter;

import org.springframework.web.servlet.HandlerInterceptor;
import support.auth.authentication.AuthenticationToken;
import support.auth.authentication.handler.AuthenticationFailureHandler;
import support.auth.authentication.handler.AuthenticationSuccessHandler;
import support.auth.authentication.provider.AuthenticationManager;
import support.auth.context.Authentication;
import support.auth.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractAuthenticationFilter implements HandlerInterceptor {
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;
    private AuthenticationManager authenticationManager;

    public AbstractAuthenticationFilter(AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler, AuthenticationManager authenticationManager) {
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        try {
            Authentication authentication = attemptAuthentication(request, response);
            successfulAuthentication(request, response, authentication);
            return getContinueChainBeforeSuccessfulAuthentication();
        } catch (Exception e) {
            unsuccessfulAuthentication(request, response, e);
            return getContinueChainBeforeUnsuccessfulAuthentication();
        }
    }

    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SecurityContextHolder.getContext().setAuthentication(authentication);

        successHandler.onAuthenticationSuccess(request, response, authentication);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, Exception failed) throws IOException {
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    protected Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticationToken token = convert(request);

        return authenticationManager.authenticate(token);
    }

    protected abstract AuthenticationToken convert(HttpServletRequest request) throws IOException;

    protected boolean getContinueChainBeforeSuccessfulAuthentication() {
        return true;
    }

    protected boolean getContinueChainBeforeUnsuccessfulAuthentication() {
        return true;
    }
}
