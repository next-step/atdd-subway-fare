package nextstep.subway.path.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FarePolicyRequest {
    int distance;

    @Builder
    private FarePolicyRequest(int distance) {
        this.distance = distance;
    }
}
