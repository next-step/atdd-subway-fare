package nextstep.subway.auth.authentication.provider;

import nextstep.subway.auth.authentication.AuthenticationException;
import nextstep.subway.auth.authentication.AuthenticationToken;
import nextstep.subway.auth.context.Authentication;
import nextstep.subway.auth.userdetails.UserDetails;
import nextstep.subway.auth.userdetails.UserDetailsService;

public class AuthenticationProvider implements AuthenticationManager {
    private UserDetailsService userDetailsService;

    public AuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, authenticationToken);

        return new Authentication(userDetails);
    }

    private void checkAuthentication(UserDetails userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkCredentials(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
