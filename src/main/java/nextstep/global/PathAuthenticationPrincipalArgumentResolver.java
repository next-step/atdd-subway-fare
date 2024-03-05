package nextstep.global;

import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.application.UserDetailService;
import nextstep.auth.application.UserDetails;
import nextstep.member.domain.AnonymousMember;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PathAuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailService userDetailService;

    public PathAuthenticationPrincipalArgumentResolver(final JwtTokenProvider jwtTokenProvider, final UserDetailService userDetailService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PathAuthenticationPrincipal.class);
    }

    @Override
    public UserDetails resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        try {
            String authorization = webRequest.getHeader("Authorization");
            if (!StringUtils.hasText(authorization)) {
                throw new AuthenticationException("토큰이 없습니다.");
            }

            if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
                throw new AuthenticationException("토큰의 형식이 올바르지 않습니다.");
            }

            String token = authorization.split(" ")[1];

            if (!jwtTokenProvider.validateToken(token)) {
                throw new AuthenticationException("토큰이 만료되었습니다.");
            }

            String email = jwtTokenProvider.getPrincipal(token);
            return userDetailService.findByEmail(email);
        } catch (Exception ex) {
            return new AnonymousMember(ex.getMessage());
        }
    }
}
