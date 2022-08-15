package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.RoleType;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader {
    private MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void loadData() {
        memberRepository.save(new Member("admin@email.com", "password", 20, Arrays.asList(RoleType.ROLE_ADMIN.name())));
        memberRepository.save(new Member("member@email.com", "password", 20, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("child@email.com", "password", 7, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("teenager@email.com", "password", 15, Arrays.asList(RoleType.ROLE_MEMBER.name())));
        memberRepository.save(new Member("nineteen@email.com", "password", 19, Arrays.asList(RoleType.ROLE_MEMBER.name())));
    }
}
