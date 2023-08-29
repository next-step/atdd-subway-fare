package nextstep.subway.path.domain.chainofresponsibility.line;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.chainofresponsibility.FarePolicy;

public class LineFarePolicy implements FarePolicy {
    @Override
    public int calculateFare(Path path) {
        return path.getAdditionalFees().stream()
                .mapToInt(fee -> fee)
                .max()
                .orElse(0);
    }
}
