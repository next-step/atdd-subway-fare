package nextstep.subway.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Getter
@RequiredArgsConstructor
public class Path {
    private final Sections sections;
    private int fare;

    public int extractDistance() {
        return sections.totalDistance();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public void addFare(final int fare) {
        this.fare = fare;
    }
}
