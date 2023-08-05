package subway.auth.principal;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import subway.auth.token.JwtTokenProvider;
import subway.constant.SubwayMessage;
import subway.exception.AuthenticationException;

@RequiredArgsConstructor
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        AuthenticationPrincipal authPrincipalAnnotation = parameter.getParameterAnnotation(AuthenticationPrincipal.class);
        boolean isRequired = authPrincipalAnnotation.required();
        String authorization = webRequest.getHeader("Authorization");
        String token = validAuthorizationAndGetToken(authorization, isRequired);

        if (token == null) {
            return null;
        }

        return getUserPrincipalFromToken(token);
    }

    private String validAuthorizationAndGetToken(String authorization, boolean isRequired) {
        if (!isValidAuthorization(authorization)) {
            if (isRequired) {
                throw new AuthenticationException(SubwayMessage.AUTH_TOKEN_NOT_FOUND_FROM_HEADERS);
            } else {
                return null;
            }
        }

        String token = authorization.split(" ")[1];

        if (!jwtTokenProvider.validateToken(token)) {
            if (isRequired) {
                throw new AuthenticationException(SubwayMessage.AUTH_INVALID_TOKEN);
            } else {
                return null;
            }
        }

        return token;
    }

    private UserPrincipal getUserPrincipalFromToken(String token) {
        String username = jwtTokenProvider.getPrincipal(token);
        String role = jwtTokenProvider.getRoles(token);

        return new UserPrincipal(username, role);
    }


    private boolean isValidAuthorization(String authorization) {
        if (authorization == null) {
            return false;
        }
        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            return false;
        }
        return true;
    }
}
