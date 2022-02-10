package support.auth.authentication.provider;

import support.auth.authentication.AuthenticationException;
import support.auth.authentication.AuthenticationToken;
import support.auth.context.Authentication;
import support.auth.userdetails.UserDetails;
import support.auth.userdetails.UserDetailsService;

public class UserDetailsAuthenticationProvider implements AuthenticationManager {
    private UserDetailsService userDetailsService;

    public UserDetailsAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, authenticationToken);

        return new Authentication(userDetails.getUsername(), userDetails.getAuthorities());
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
