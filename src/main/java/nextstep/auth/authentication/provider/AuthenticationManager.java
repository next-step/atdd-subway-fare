package nextstep.auth.authentication.provider;


import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;

public interface AuthenticationManager {
    Authentication authenticate(AuthenticationToken authenticationToken);
}
