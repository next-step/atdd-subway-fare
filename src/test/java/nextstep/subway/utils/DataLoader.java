package nextstep.subway.utils;

import static nextstep.subway.utils.GithubResponses.*;

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
        memberRepository.save(new Member(사용자_12세.getEmail(), "password", 사용자_12세.getAge(), RoleType.ROLE_MEMBER.name()));
        memberRepository.save(new Member(사용자_13세.getEmail(), "password", 사용자_13세.getAge(), RoleType.ROLE_MEMBER.name()));
        memberRepository.save(new Member(사용자_14세.getEmail(), "password", 사용자_14세.getAge(), RoleType.ROLE_MEMBER.name()));
        memberRepository.save(new Member(사용자_19세.getEmail(), "password", 사용자_19세.getAge(), RoleType.ROLE_MEMBER.name()));
        memberRepository.save(new Member(사용자_20세.getEmail(), "password", 사용자_20세.getAge(), RoleType.ROLE_MEMBER.name()));
    }
}
