package nextstep.subway.utils;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class DataLoader {
    private MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 20, RoleType.ROLE_ADMIN.name()));
        memberRepository.save(new Member("member@email.com", "password", 20, RoleType.ROLE_MEMBER.name()));
        memberRepository.save(new Member(GithubResponses.사용자1.getEmail(), "password", 5, RoleType.ROLE_MEMBER.name()));
        memberRepository.save(new Member(GithubResponses.사용자2.getEmail(), "password", 10, RoleType.ROLE_MEMBER.name()));
        memberRepository.save(new Member(GithubResponses.사용자3.getEmail(), "password", 15, RoleType.ROLE_MEMBER.name()));
        memberRepository.save(new Member(GithubResponses.사용자4.getEmail(), "password", 20, RoleType.ROLE_MEMBER.name()));
    }
}