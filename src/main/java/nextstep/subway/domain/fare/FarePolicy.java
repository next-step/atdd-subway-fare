package nextstep.subway.domain.fare;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.subway.domain.Path;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FarePolicy {

    private final DistanceFarePolicy distanceFarePolicy;
    private final LineFarePolicy lineFarePolicy;
    private final AgeFarePolicy ageFarePolicy;
    private final MemberService memberService;

    public int getFare(final Path path, final LoginMember loginMember) {
        final int fare = distanceFarePolicy.getFare(path.extractDistance()) + lineFarePolicy.getFare(path);
        if (loginMember.getId() == null) {
            return fare;
        }

        int age = memberService.findMember(loginMember.getId())
                .getAge();

        return ageFarePolicy.discountUnderTeenager(age, fare);
    }
}
