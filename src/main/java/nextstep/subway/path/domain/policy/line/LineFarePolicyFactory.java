package nextstep.subway.path.domain.policy.line;

import static nextstep.subway.path.domain.policy.line.LineFareFirstPolicy.TEN_KM_DISTANCE;
import static nextstep.subway.path.domain.policy.line.LineFareSecondPolicy.FIFTY_KM_DISTANCE;

public class LineFarePolicyFactory {

    public static LineFarePolicy from(int distance) {
        if (distance > TEN_KM_DISTANCE && distance <= FIFTY_KM_DISTANCE) {
            return new LineFareFirstPolicy();
        }
        if (distance > FIFTY_KM_DISTANCE) {
            return new LineFareSecondPolicy(new LineFareFirstPolicy());
        }
        return new LineFareDefaultPolicy();
    }
}
