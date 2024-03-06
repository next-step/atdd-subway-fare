package nextstep.member.application.service;

import nextstep.auth.application.service.UserDetailService;
import nextstep.auth.domain.UserDetail;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.entity.Member;
import nextstep.member.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserDetailServiceImpl implements UserDetailService {
    private final MemberRepository memberRepository;

    public UserDetailServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetail getUserDetailByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보가 존재하지 않습니다."));
        return new UserDetail(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }

    @Override
    public UserDetail createUserIfNotExist(UserDetail userDetail) {
        try {
            return getUserDetailByEmail(userDetail.getEmail());
        } catch (EntityNotFoundException e) {
            Member member = memberRepository.save(new Member(userDetail.getEmail(), userDetail.getPassword(), userDetail.getAge()));
            return new UserDetail(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
        }
    }
}
