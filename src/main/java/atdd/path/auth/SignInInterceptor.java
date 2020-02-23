package atdd.path.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SignInInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public SignInInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.split(" ")[1];
            String email = jwtTokenProvider.parseToken(token);
            request.setAttribute("loginUserEmail", email);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
