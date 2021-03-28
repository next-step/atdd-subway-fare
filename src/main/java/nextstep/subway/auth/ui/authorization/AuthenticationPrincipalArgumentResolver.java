package nextstep.subway.auth.ui.authorization;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;

public abstract class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(getAuthenticationAnnotation());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return getPrincipalByAuthentication(
            parameter,
            getAuthentication()
        );
    }

    protected Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    abstract <A extends Annotation> Class<A> getAuthenticationAnnotation();
    abstract protected Object getPrincipalByAuthentication(MethodParameter parameter, Authentication authentication);

    protected Object extractPrincipal(MethodParameter parameter, Authentication authentication) {
        try {
            Map<String, String> principal = (Map) authentication.getPrincipal();

            Object[] params = Arrays.stream(parameter.getParameterType().getDeclaredFields())
                    .map(it -> toObject(it.getType(), principal.get(it.getName())))
                    .toArray();

            return parameter.getParameterType().getConstructors()[0].newInstance(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static Object toObject(Class clazz, String value) {
        if (Boolean.class == clazz) return Boolean.parseBoolean(value);
        if (Byte.class == clazz) return Byte.parseByte(value);
        if (Short.class == clazz) return Short.parseShort(value);
        if (Integer.class == clazz) return Integer.parseInt(value);
        if (Long.class == clazz) return Long.parseLong(value);
        if (Float.class == clazz) return Float.parseFloat(value);
        if (Double.class == clazz) return Double.parseDouble(value);
        return value;
    }
}
