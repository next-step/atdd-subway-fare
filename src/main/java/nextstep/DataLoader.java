package nextstep;

import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader {
    private MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public void saveMemberData(List<Member> members) {
        memberRepository.saveAll(members);
    }

    public void loadData() {
    }
}
