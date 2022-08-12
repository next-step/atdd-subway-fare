package nextstep.auth.authentication.filter;

import nextstep.auth.authentication.handler.AuthenticationSuccessHandler;
import org.apache.tomcat.util.codec.binary.Base64;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.authentication.handler.AuthenticationFailureHandler;
import nextstep.auth.authentication.provider.AuthenticationManager;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthenticationFilter extends AbstractAuthenticationFilter {

    public BasicAuthenticationFilter(AuthenticationSuccessHandler successHandler,
                                     AuthenticationFailureHandler failureHandler,
                                     AuthenticationManager authenticationManager) {
        super(successHandler, failureHandler, authenticationManager);
    }

    @Override
    protected AuthenticationToken convert(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        return new AuthenticationToken(principal, credentials);
    }

    @Override
    protected boolean shouldContinueChainAfterSuccessfulAuthentication() {
        return true;
    }

    @Override
    protected boolean shouldContinueChainAfterUnSuccessfulAuthentication() {
        return true;
    }
}
