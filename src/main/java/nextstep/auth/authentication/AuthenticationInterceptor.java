package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.DetailMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nextstep.subway.ui.exception.ExceptionMessage.INVALID_PASSWORD;
import static nextstep.subway.ui.exception.ExceptionMessage.NOT_EXISTS_EMAIL;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {
    private final UserDetailService userDetailsService;
    private final AuthenticationTokenConverter authenticationTokenConverter;

    protected AuthenticationInterceptor(UserDetailService userDetailService, AuthenticationTokenConverter authenticationTokenConverter) {
        this.userDetailsService = userDetailService;
        this.authenticationTokenConverter = authenticationTokenConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = authenticationTokenConverter.convert(request);
        Authentication authentication = authenticate(authenticationToken);
        afterAuthentication(request, response, authentication);

        return false;
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        DetailMember detailMember = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(detailMember, token);

        return new Authentication(detailMember);
    }

    // 리뷰 코멘트 질문 : 컨트롤러에 요청 가기 전에 예외 메시지 응답하는 방법
    private void checkAuthentication(DetailMember detailMember, AuthenticationToken token) {
        if (detailMember == null) {
            throw new AuthenticationException(NOT_EXISTS_EMAIL.getMsg());
        }

        if (!detailMember.checkPassword(token.getCredentials())) {
            throw new AuthenticationException(INVALID_PASSWORD.getMsg());
        }
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;
}
