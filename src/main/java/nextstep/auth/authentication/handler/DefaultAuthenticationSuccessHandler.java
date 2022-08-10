package nextstep.auth.authentication.handler;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
