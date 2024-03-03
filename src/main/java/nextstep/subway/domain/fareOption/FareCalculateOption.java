package nextstep.subway.domain.fareOption;

import java.util.Set;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;

public interface FareCalculateOption {
    boolean isCalculateTarget(int distance, Set<LineResponse> line);
    int calculateFare(int distance, Set<LineResponse> line);
}
