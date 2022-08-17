package nextstep.subway.util.fare;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Path;

public class OverFiftyKiloMeterFare extends FareChain {
    private static final int FIFTY_KILOMETER = 50;

    @Override
    public int calculate(Path path, Member member) {
        int distance = path.extractDistance();
        int surcharge = 0;
        if (distance > FIFTY_KILOMETER) {
            surcharge += calculateOverFare(distance);
        }
        return surcharge + super.calculate(path, member);
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - FIFTY_KILOMETER - 1) / 5) + 1) * 100);
    }
}
