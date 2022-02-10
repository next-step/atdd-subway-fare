package support.auth.authentication.filter;

import org.apache.tomcat.util.codec.binary.Base64;
import support.auth.authentication.AuthenticationToken;
import support.auth.authentication.AuthorizationExtractor;
import support.auth.authentication.AuthorizationType;
import support.auth.authentication.handler.AuthenticationFailureHandler;
import support.auth.authentication.handler.AuthenticationSuccessHandler;
import support.auth.authentication.provider.AuthenticationManager;

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
}
