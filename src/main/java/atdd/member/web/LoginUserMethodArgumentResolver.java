package atdd.member.web;

import atdd.member.application.MemberService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static atdd.member.domain.Guest.GUEST_MEMBER;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@Component
public class LoginUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;

    public LoginUserMethodArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String email = (String) webRequest.getAttribute("loginUserEmail", SCOPE_REQUEST);
        if (Strings.isBlank(email)) {
            return GUEST_MEMBER;
        }

        try {
            return memberService.findMemberByEmail(email);
        } catch (Exception e) {
            return GUEST_MEMBER;
        }
    }
}
