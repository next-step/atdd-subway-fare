package nextstep.path.applicaion;

import nextstep.auth.userdetails.User;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.path.domain.fare.NonDiscountMember;
import org.springframework.stereotype.Service;

@Service
public class PathMemberService {

    private final MemberService memberService;

    public PathMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    public Member findMemberForDiscountByUser(User user) {
        return memberService.findMemberByUser(user)
                .orElse(NonDiscountMember.getInstance());
    }
}
