package nextstep.global;

import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.application.UserDetailService;
import nextstep.auth.application.UserDetails;
import nextstep.member.domain.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private JwtTokenProvider jwtTokenProvider;
    private UserDetailService userDetailService;

    public AuthenticationPrincipalArgumentResolver(final JwtTokenProvider jwtTokenProvider, final UserDetailService userDetailService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public LoginMember resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        try {
            String authorization = webRequest.getHeader("Authorization");

            String token = validationAndCreateToken(authorization);

            String email = jwtTokenProvider.getPrincipal(token);

            final UserDetails userDetails = userDetailService.findByEmail(email);
            return new LoginMember(email, userDetails.getAge());
        } catch (AuthenticationException ex) {
            if (isAuthenticate(parameter)) {
                throw ex;
            }
        }

        return new LoginMember(null, null);
    }

    private String validationAndCreateToken(String authorization) {
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
        return token;
    }

    private boolean isAuthenticate(MethodParameter parameter) {
        AuthenticationPrincipal parameterAnnotation = parameter.getParameterAnnotation(AuthenticationPrincipal.class);
        return parameterAnnotation.required();
    }
}
