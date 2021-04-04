package nextstep.subway.auth.application;

import org.springframework.stereotype.Service;

@Service
public interface AnonymousUserDetailService {
  Object getAnonymousUser();
}
