package nextstep.subway.util.fare;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Path;

public class OverTenKiloMeterFare extends FareChain {
    private static final int TEN_KILOMETER = 10;

    @Override
    public int calculate(Path path, Member member) {
        int distance = path.extractDistance();
        if (distance > TEN_KILOMETER) {
            return calculateOverFare(distance) + super.calculate(path, member);
        }
        return super.calculate(path, member);
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - TEN_KILOMETER - 1) / 5) + 1) * 100);
    }
}
