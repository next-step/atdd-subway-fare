package nextstep.subway.member.application;
import nextstep.subway.auth.application.AnonymousUserDetailService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.stereotype.Service;

@Service
public class CustomAnonymousUserDetailService implements AnonymousUserDetailService {

  private static final LoginMember ANONYMOUS_USER = new LoginMember(0L,"","",0);

  @Override
  public Object getAnonymousUser() {
    return ANONYMOUS_USER;
  }
}
