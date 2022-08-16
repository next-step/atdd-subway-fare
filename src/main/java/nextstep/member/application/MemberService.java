package nextstep.member.application;

import nextstep.auth.userdetails.User;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.MemberUpdateRequest;
import nextstep.member.domain.exception.DuplicateEmailException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException();
        }

        Member member = memberRepository.save(request.toEntity());
        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse findMemberResponse(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);
        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findMemberByUser(User user) {
        return memberRepository.findByEmail(user.getUsername());
    }

    public void updateMember(String email, MemberUpdateRequest param) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);
        member.update(param.toEntity());
    }

    public void deleteMember(String email) {
        memberRepository.deleteByEmail(email);
    }
}