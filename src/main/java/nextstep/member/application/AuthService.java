package nextstep.member.application;

import nextstep.exception.NotFoundException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
