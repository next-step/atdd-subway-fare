package nextstep.subway.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class Path {
    private List<Station> path;
    private Long distance;
    private Long duration;
    private Long additionalFare;

    @Builder
    public Path(List<Station> path, Long distance, Long duration, Long additionalFare) {
        this.path = path;
        this.distance = distance;
        this.duration = duration;
        this.additionalFare = additionalFare;
    }
}
