package nextstep.subway.members.member.application;

import nextstep.subway.auth.application.UserDetails;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.members.member.domain.EmptyMember;
import nextstep.subway.members.member.domain.LoginMember;
import nextstep.subway.members.member.domain.Member;
import nextstep.subway.members.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMember loadUserByUsername(String principal) {
        Member member = memberRepository.findByEmail(principal).orElseThrow(RuntimeException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }

    @Override
    public UserDetails emptyMember() {
        return new EmptyMember();
    }

}
