package nextstep.member.ui;

import java.util.List;
import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import nextstep.member.application.JwtTokenProvider;
import nextstep.member.application.MemberService;
import nextstep.member.domain.AuthenticationPrincipal;
import nextstep.member.domain.Guest;
import nextstep.member.domain.LoginMember;

@Component
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
	private JwtTokenProvider jwtTokenProvider;
	private MemberService memberService;

	public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String authorization = webRequest.getHeader("Authorization");
		if (Objects.isNull(authorization)) {
			return Guest.getInstance();
		}
		if (!"bearer".equalsIgnoreCase(authorization.split(" ")[0])) {
			throw new AuthenticationException();
		}
		String token = authorization.split(" ")[1];

		if (!jwtTokenProvider.validateToken(token)) {
			return Guest.getInstance();
		}

		Long id = Long.parseLong(jwtTokenProvider.getPrincipal(token));
		List<String> roles = jwtTokenProvider.getRoles(token);

		return new LoginMember(id, roles);
	}
}
