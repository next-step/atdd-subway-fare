package nextstep.auth.authentication.filter;

import nextstep.auth.authentication.handler.AuthenticationSuccessHandler;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.authentication.handler.AuthenticationFailureHandler;
import nextstep.auth.authentication.provider.AuthenticationManager;

import javax.servlet.http.HttpServletRequest;

public class BearerTokenAuthenticationFilter extends AbstractAuthenticationFilter {

    public BearerTokenAuthenticationFilter(AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler, AuthenticationManager authenticationManager) {
        super(successHandler, failureHandler, authenticationManager);
    }

    @Override
    protected AuthenticationToken convert(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        return new AuthenticationToken(authCredentials, authCredentials);
    }
}
