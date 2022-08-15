package nextstep.subway.domain.policy;

import lombok.Builder;

@Builder
public class PathByFare {

    private int distance;

    public int distance() {
        return distance;
    }
}
