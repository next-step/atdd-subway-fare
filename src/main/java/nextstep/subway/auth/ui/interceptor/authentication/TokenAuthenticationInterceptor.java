package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.application.handler.AuthenticationFailureHandler;
import nextstep.subway.auth.application.handler.AuthenticationSuccessHandler;
import nextstep.subway.auth.application.provider.AuthenticationManager;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

public class TokenAuthenticationInterceptor extends AbstractAuthenticationInterceptor {
    private static final String REGEX = ":";

    public TokenAuthenticationInterceptor(AuthenticationManager authenticationManager, AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler) {
        super(authenticationManager, successHandler, failureHandler);
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);

        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String decodedString = new String(decodedBytes);

        String principal = decodedString.split(REGEX)[0];
        String credentials = decodedString.split(REGEX)[1];

        return new AuthenticationToken(principal, credentials);
    }
}
