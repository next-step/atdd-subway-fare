package atdd.user.web;

import atdd.security.InvalidJwtAuthenticationException;
import atdd.security.JwtTokenProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private static final String AUTHORIZATION = "Authorization";
    private static final String LOGIN_USER_EMAIL = "loginUserEmail";

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(AUTHORIZATION);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidJwtAuthenticationException("Invalid token");
        }

        String email = jwtTokenProvider.getUserEmail(token);
        request.setAttribute(LOGIN_USER_EMAIL, email);
        return true;
    }
}
