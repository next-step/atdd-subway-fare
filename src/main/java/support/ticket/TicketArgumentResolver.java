package support.ticket;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import support.auth.context.Authentication;
import support.auth.context.SecurityContextHolder;

public class TicketArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    public TicketArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Ticket.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return TicketType.STANDARD;
        }

        Member member = memberRepository.findByEmail(String.valueOf(authentication.getPrincipal()))
                .orElseThrow(IllegalArgumentException::new);
        return TicketType.of(member.getAge());
    }
}
