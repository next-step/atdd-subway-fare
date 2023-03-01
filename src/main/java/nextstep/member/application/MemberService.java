package nextstep.member.application;

import nextstep.exception.NotFoundException;
import nextstep.exception.UnAuthorizedException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member login(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundException::new);
        if (!member.checkPassword(password)) {
            throw new UnAuthorizedException();
        }

        return member;
    }

    public Member createOrFindMember(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.orElseGet(() -> memberRepository.save(new Member(email, "", 0)));

    }
}