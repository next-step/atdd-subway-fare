package nextstep.subway.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class Path {
    private List<Station> path;
    private Long distance;
    private Long duration;
    private int fare;

    @Builder
    public Path(List<Station> path, Long distance, Long duration, int fare) {
        this.path = path;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }
}
