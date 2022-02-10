package support.auth.authentication.provider;

import support.auth.authentication.AuthenticationToken;
import support.auth.context.Authentication;

public interface AuthenticationManager {
    Authentication authenticate(AuthenticationToken authenticationToken);
}
