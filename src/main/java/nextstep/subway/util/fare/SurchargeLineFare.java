package nextstep.subway.util.fare;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Path;

public class SurchargeLineFare extends FareChain {

    @Override
    public int calculate(Path path, Member member) {
        int surcharge = path.getPassingLines().stream()
                .mapToInt(Line::getSurcharge)
                .max().getAsInt();
        return surcharge + super.calculate(path, member);
    }
}
