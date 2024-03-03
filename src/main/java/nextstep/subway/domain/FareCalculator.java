package nextstep.subway.domain;

import java.util.Set;
import nextstep.subway.applicaion.dto.LineResponse;

public interface FareCalculator {
    int calculateFare(int distance, Set<LineResponse> lines);
}
