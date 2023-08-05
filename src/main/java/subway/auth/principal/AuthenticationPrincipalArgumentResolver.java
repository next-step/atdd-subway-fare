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

        if (!isValidAuthorization(authorization)) {
            if (isRequired) {
                throw new AuthenticationException(SubwayMessage.AUTH_TOKEN_NOT_FOUND_FROM_HEADERS);
            } else {
                // Not required, return null if authorization is not valid
                return null;
            }
        }

        String token = authorization.split(" ")[1];

        if (!jwtTokenProvider.validateToken(token)) {
            if (isRequired) {
                throw new AuthenticationException(SubwayMessage.AUTH_INVALID_TOKEN);
            } else {
                // Not required, return null if token is not valid
                return null;
            }
        }

        String username = jwtTokenProvider.getPrincipal(token);
        String role = jwtTokenProvider.getRoles(token);

        return new UserPrincipal(username, role);
    }

    private static boolean isValidAuthorization(String authorization) {
        if (authorization == null) {
            return false;
        }
        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            return false;
        }
        return true;
    }

    private void validToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException(SubwayMessage.AUTH_INVALID_TOKEN);
        }
    }

    private static void validAuthorization(String authorization) {
        if (authorization == null) {
            throw new AuthenticationException(SubwayMessage.AUTH_TOKEN_NOT_FOUND_FROM_HEADERS);
        }
        if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
            throw new AuthenticationException(SubwayMessage.AUTH_TOKEN_NOT_FOUND_FROM_HEADERS);
        }
    }
}
