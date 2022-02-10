package support.auth.authentication.filter;

import support.auth.authentication.AuthenticationToken;
import support.auth.authentication.AuthorizationExtractor;
import support.auth.authentication.AuthorizationType;
import support.auth.authentication.handler.AuthenticationFailureHandler;
import support.auth.authentication.handler.AuthenticationSuccessHandler;
import support.auth.authentication.provider.AuthenticationManager;

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
