package support.auth.authentication.provider;

import support.auth.authentication.AuthenticationException;
import support.auth.authentication.AuthenticationToken;
import support.auth.context.Authentication;
import support.auth.token.JwtTokenProvider;

import java.util.List;

public class TokenAuthenticationProvider implements AuthenticationManager {
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        if (!jwtTokenProvider.validateToken(authenticationToken.getPrincipal())) {
            throw new AuthenticationException();
        }

        String principal = jwtTokenProvider.getPrincipal(authenticationToken.getPrincipal());
        List<String> roles = jwtTokenProvider.getRoles(authenticationToken.getPrincipal());

        Authentication authentication = new Authentication(principal, roles);
        return authentication;
    }
}
