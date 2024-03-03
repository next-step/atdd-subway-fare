package nextstep.subway.domain.fareOption;

import java.util.Set;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;

public class FareExtraFareDistanceOption implements FareCalculateOption {

    @Override
    public boolean isCalculateTarget(int distance, Set<LineResponse> line) {
        return true;
    }

    @Override
    public int calculateFare(int distance, Set<LineResponse> lines) {
        return lines.stream().mapToInt(LineResponse::getExtraFare).max().orElse(0);
    }
}
