package nextstep.subway.domain;

import static nextstep.subway.domain.FareCalculation.*;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Path {
    private Sections sections;

    public int extractDistance() {
        return sections.totalDistance();
    }

	public int extractDuration() {
		return sections.totalDuration();
	}

	public int extractFare() {
		return fareCalculation(extractDistance());
	}

    public List<Station> getStations() {
        return sections.getStations();
    }
}
