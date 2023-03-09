package nextstep.subway.utils;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Profile("test")
@Component
public class DataLoader {
    private MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 20, Arrays.asList(RoleType.ROLE_ADMIN.name())));
        memberRepository.save(new Member("member@email.com", "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("청소년@email.com", "password", 18, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("어린이@email.com", "password", 12, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(GithubResponses.사용자1.getEmail(), "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(GithubResponses.사용자2.getEmail(), "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(GithubResponses.사용자3.getEmail(), "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(GithubResponses.사용자4.getEmail(), "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
    }
}
