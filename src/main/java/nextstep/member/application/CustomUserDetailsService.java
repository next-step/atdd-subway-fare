package nextstep.member.application;

import nextstep.auth.userdetails.UserDetailsService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.NonLoginMember;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMember loadUserByUsername(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return LoginMember.of(member);
    }

    @Override
    public NonLoginMember getNonLoginMember() {
        return NonLoginMember.getInstance();
    }
}
