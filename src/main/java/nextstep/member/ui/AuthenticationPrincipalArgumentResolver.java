package nextstep.member.ui;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.context.SecurityContextHolder;
import nextstep.member.domain.AnonymousUser;
import nextstep.member.domain.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;
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
        boolean required = parameter.getParameterAnnotation(AuthenticationPrincipal.class).required();
        String authorization = webRequest.getHeader("Authorization");

        if (required) {
            if (StringUtils.hasText(authorization)) {
                return getLoginMember(authorization);
            }
        }

        if (!required) {
            if (StringUtils.hasText(authorization)) {
                return getLoginMember(authorization);
            } else {
                return getAnonymousUser();
            }
        }

        throw new RuntimeException();
    }

    private AnonymousUser getAnonymousUser() {
        String anonymousToken = SecurityContextHolder.getContext().getAuthentication();
        jwtTokenProvider.validateToken(anonymousToken);

        return new AnonymousUser();
    }

    private LoginMember getLoginMember(String authorization) {
        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException();
        }
        String token = authorization.split(" ")[1];

        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(token));
        Member member = memberService.getMember(id);
        return new LoginMember(member.getId(), member.getAge(), member.getRoles());
    }
}
