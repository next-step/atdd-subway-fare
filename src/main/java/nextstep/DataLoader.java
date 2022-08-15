package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader {
    private static final String PASSWORD ="password";

    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member("admin@email.com", PASSWORD, 20, List.of(RoleType.ROLE_ADMIN.name())));

        memberRepository.save(new Member("child@email.com", PASSWORD, 9, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("teenager@email.com", PASSWORD, 17, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("member@email.com", PASSWORD, 30, List.of(RoleType.ROLE_MEMBER.name())));
    }
}
