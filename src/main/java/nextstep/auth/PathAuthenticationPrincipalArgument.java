package nextstep.auth;

import io.jsonwebtoken.JwtException;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.application.UserDetailService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PathAuthenticationPrincipalArgument implements HandlerMethodArgumentResolver {

    private JwtTokenProvider jwtTokenProvider;
    private UserDetailService userDetailService;
    public PathAuthenticationPrincipalArgument(JwtTokenProvider jwtTokenProvider, UserDetailService userDetailService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PathAuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorization = webRequest.getHeader("Authorization");
        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException();
        }
        String token = authorization.split(" ")[1];
        if(!jwtTokenProvider.validateToken(token)){
            throw new AuthenticationException();
        }

        try {
            String email = jwtTokenProvider.getPrincipal(token);
            return userDetailService.loadUser(email);
        } catch (JwtException e) {
            throw new AuthenticationException();
        }
    }
}
