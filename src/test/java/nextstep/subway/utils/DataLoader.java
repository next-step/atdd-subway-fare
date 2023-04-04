package nextstep.subway.utils;

import static nextstep.subway.fixture.MemberFixture.관리자_ADMIN;
import static nextstep.subway.fixture.MemberFixture.성인_회원_ALEX;
import static nextstep.subway.fixture.MemberFixture.어린이_회원_BEOM;
import static nextstep.subway.fixture.MemberFixture.청소년_회원_JADE;

import java.util.Arrays;
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
        memberRepository.save(new Member("admin@email.com", "password", 20, Arrays.asList(RoleType.ROLE_ADMIN.name())));
        memberRepository.save(new Member("member@email.com", "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(GithubResponses.사용자1.getEmail(), "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(GithubResponses.사용자2.getEmail(), "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(GithubResponses.사용자3.getEmail(), "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member(GithubResponses.사용자4.getEmail(), "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));

        memberRepository.save(성인_회원_ALEX.엔티티_생성());
        memberRepository.save(청소년_회원_JADE.엔티티_생성());
        memberRepository.save(어린이_회원_BEOM.엔티티_생성());
        memberRepository.save(관리자_ADMIN.엔티티_생성());
    }
}
