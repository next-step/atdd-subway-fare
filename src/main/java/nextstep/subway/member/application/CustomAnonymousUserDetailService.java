package nextstep.subway.member.application;

import nextstep.subway.auth.application.AnonymousUserDetailService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.stereotype.Service;

@Service
public class CustomAnonymousUserDetailService implements AnonymousUserDetailService {

    private static final LoginMember nonLoginMember = new LoginMember(0L, "", "", 30);

    @Override
    public LoginMember getNonLoginMember() {
        return nonLoginMember;
    }
}
