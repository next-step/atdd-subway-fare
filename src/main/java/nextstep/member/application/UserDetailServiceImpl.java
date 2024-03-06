package nextstep.member.application;

import nextstep.auth.application.UserDetail;
import nextstep.auth.application.UserDetailService;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    private final MemberRepository memberRepository;

    public UserDetailServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetail loadUser(String userId) {
        return memberRepository.findByEmail(userId)
                .map(member -> new UserDetail(member.getEmail(), member.getPassword(), member.getAge()))
                .orElse(UserDetail.EMPTY);
    }

    @Override
    public UserDetail saveUser(String userId, String password, int age) {
        Member saved = memberRepository.save(new Member(userId, password, age));
        return new UserDetail(saved.getEmail(), saved.getPassword(), age);
    }

}
