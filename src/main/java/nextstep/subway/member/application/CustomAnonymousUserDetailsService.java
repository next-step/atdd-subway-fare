package nextstep.subway.member.application;

import nextstep.subway.auth.application.AnonymousUserDetailService;
import nextstep.subway.auth.application.UserDetails;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomAnonymousUserDetailsService implements AnonymousUserDetailService {

    private final Member ANNONYMOUS_MEMBER  = new Member("", "", 0);

    @Override
    public UserDetails getAnonymousUser() {
        return LoginMember.of(ANNONYMOUS_MEMBER);
    }
}
