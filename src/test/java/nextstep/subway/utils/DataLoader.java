package nextstep.subway.utils;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;

@ActiveProfiles("test")
@Component
public class DataLoader {
    private MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 20, List.of(RoleType.ROLE_ADMIN.name())));
        memberRepository.save(new Member("member@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(GithubResponses.사용자1.getEmail(), "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(GithubResponses.사용자2.getEmail(), "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(GithubResponses.사용자3.getEmail(), "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(GithubResponses.사용자4.getEmail(), "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("adult@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("child@email.com", "password", 13, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("teenager@email.com", "password", 12, List.of(RoleType.ROLE_MEMBER.name())));
    }
}
