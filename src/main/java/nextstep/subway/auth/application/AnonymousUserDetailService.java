package nextstep.subway.auth.application;

import org.springframework.stereotype.Service;

public interface AnonymousUserDetailService {
    UserDetails getAnonymousUser();
}
