package nextstep.auth.authentication.handler;

import nextstep.auth.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, Exception failed) throws Exception {
        SecurityContextHolder.clearContext();
        throw failed; // 예외가 무시되지 않도록 AbstractAuthenticationFilter 에서 잡은 Exception 다시 throw
    }
}
