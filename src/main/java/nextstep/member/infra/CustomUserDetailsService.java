package nextstep.member.infra;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import support.auth.userdetails.User;
import support.auth.userdetails.UserDetails;
import support.auth.userdetails.UserDetailsService;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Member member = memberRepository
                .findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);

        return new User(member.getEmail(), member.getPassword(), member.getRoles());
    }
}
