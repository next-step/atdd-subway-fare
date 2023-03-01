package nextstep.subway.path;

import static nextstep.subway.domain.FareCalculation.*;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

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
