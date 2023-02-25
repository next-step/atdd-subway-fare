package nextstep.member.application;

import nextstep.common.exception.EmailInputException;
import nextstep.common.exception.PasswordMatchException;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static nextstep.common.error.MemberError.*;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member login(String email, String password) {
        final Member member = findByEmail(email);
        if (!member.checkPassword(password)) {
            throw new PasswordMatchException(UNAUTHORIZED);
        }
        return member;
    }

    public Member createOrFindMember(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isEmpty()) {
            return memberRepository.save(new Member(email, "", 0));
        }
        return member.get();
    }

    public MemberResponse findMemberOfMine(final String email) {
        final Member findMember = findByEmail(email);
        return MemberResponse.of(findMember);
    }

    public Member findByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new EmailInputException(NOT_INPUT_EMAIL));
    }
}