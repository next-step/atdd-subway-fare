package nextstep.auth.authentication;

import nextstep.auth.authentication.handler.AuthenticationFailureHandler;
import nextstep.auth.authentication.handler.AuthenticationSuccessHandler;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractAuthenticationInterceptor implements HandlerInterceptor {
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;

    protected AbstractAuthenticationInterceptor(AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler) {
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        try {
            Authentication authentication = attemptAuthentication(request, response);
            successfulAuthentication(request, response, authentication);
        } catch (AuthenticationException e) {
            unsuccessfulAuthentication(request, response, e);
        }

        return false;
    }

    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SecurityContextHolder.getContext().setAuthentication(authentication);

        successHandler.onAuthenticationSuccess(request, response, authentication);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed) throws IOException {
        SecurityContextHolder.clearContext();

        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    protected abstract Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
