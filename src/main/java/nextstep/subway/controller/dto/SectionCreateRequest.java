package nextstep.subway.controller.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class SectionCreateRequest {
    @NotEmpty
    private String upStationId;
    @NotEmpty
    private String downStationId;
    @NotNull
    private Long distance;
    @NotNull
    private Long duration;

    @Builder
    public SectionCreateRequest(String upStationId, String downStationId, Long distance, Long duration) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.duration = duration;
    }
}
