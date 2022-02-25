package nextstep.subway.path.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FarePolicyRequest {
    private int distance;
    private int lineExtraChargeMax;

    @Builder
    private FarePolicyRequest(int distance, int lineExtraChargeMax) {
        this.distance = distance;
        this.lineExtraChargeMax = lineExtraChargeMax;
    }
}
