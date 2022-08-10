package nextstep.auth.authentication.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, Exception failed) throws IOException {
        // 아무 처리도 하지 않고 다음 인터셉로 이동
    }
}
