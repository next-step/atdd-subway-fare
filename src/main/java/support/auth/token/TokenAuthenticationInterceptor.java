package support.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import support.auth.authentication.AuthenticationToken;
import support.auth.authentication.filter.AbstractAuthenticationFilter;
import support.auth.authentication.handler.AuthenticationFailureHandler;
import support.auth.authentication.handler.AuthenticationSuccessHandler;
import support.auth.authentication.provider.AuthenticationManager;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public class TokenAuthenticationInterceptor extends AbstractAuthenticationFilter {
    public TokenAuthenticationInterceptor(AuthenticationSuccessHandler successHandler,
                                          AuthenticationFailureHandler failureHandler,
                                          AuthenticationManager authenticationManager) {
        super(successHandler, failureHandler, authenticationManager);
    }

    @Override
    protected AuthenticationToken convert(HttpServletRequest request) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        return new AuthenticationToken(principal, credentials);
    }

    @Override
    protected boolean getContinueChainBeforeSuccessfulAuthentication() {
        return false;
    }

    @Override
    protected boolean getContinueChainBeforeUnsuccessfulAuthentication() {
        return false;
    }
}
