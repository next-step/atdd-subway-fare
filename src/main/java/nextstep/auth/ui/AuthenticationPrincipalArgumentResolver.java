package nextstep.auth.ui;

import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.domain.UserDetail;
import nextstep.exception.AuthenticationException;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;


public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private JwtTokenProvider jwtTokenProvider;

    private final MemberService memberService;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader("Authorization");
        boolean required = Objects.requireNonNull(parameter.getParameterAnnotation(AuthenticationPrincipal.class)).required();
        if (!required && authorization == null) {
            return new UserDetail("", -1);
        }
        existToken(authorization);
        String token = authorization.split(" ")[1];
        checkValidToken(token);

        String email = jwtTokenProvider.getPrincipal(token);
        Member member = memberService.findMemberByEmail(email);
        return new UserDetail(member.getEmail(), member.getAge());
    }

    private void existToken(String authorization) {
        if (!authorization.split(" ")[0].equalsIgnoreCase("bearer")
                || authorization.split(" ").length == 1) {
            throw new AuthenticationException("존재하지 않는 토큰의 인증입니다.");
        }
    }

    private void checkValidToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException("유효한 토큰이 아닙니다.");
        }
    }
}
