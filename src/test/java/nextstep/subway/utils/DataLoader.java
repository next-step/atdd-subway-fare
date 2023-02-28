package nextstep.subway.utils;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static nextstep.subway.utils.GithubResponses.*;

@ActiveProfiles("test")
@Component
public class DataLoader {
    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 20, Arrays.asList(RoleType.ROLE_ADMIN.name())));
        memberRepository.save(new Member("member@email.com", "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(어린이.getEmail(), "password", 어린이.getAge(), Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(청소년.getEmail(), "password", 청소년.getAge(), Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(사용자3.getEmail(), "password", 사용자3.getAge(), Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(사용자4.getEmail(), "password", 사용자4.getAge(), Arrays.asList(RoleType.ROLE_MEMBER.name())));
    }
}
