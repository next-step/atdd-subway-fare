package nextstep.subway.auth.authentication.provider;


import nextstep.subway.auth.authentication.AuthenticationToken;
import nextstep.subway.auth.context.Authentication;

public interface AuthenticationManager {
    Authentication authenticate(AuthenticationToken authenticationToken);
}
