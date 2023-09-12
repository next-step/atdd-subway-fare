package nextstep.subway.utils.path;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Path;

@RequiredArgsConstructor
public class PathFareProvider {

    private final PathFareCalculator overFareCalculator;

    public Integer calculateFare(Path path) {
        Long distance = path.getSections().getTotalDistance();
        return 1250 + overFareCalculator.calculate(distance);
    }
}
