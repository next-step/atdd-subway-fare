package nextstep;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DataLoader {
    private final MemberRepository memberRepository;

    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 20, List.of(RoleType.ROLE_ADMIN.name())));
        memberRepository.save(new Member("member@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("adult@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("teenager@email.com", "password", 18, List.of(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("children@email.com", "password", 12, List.of(RoleType.ROLE_MEMBER.name())));
    }
}
