package nextstep.subway.domain;

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

    public List<Station> getStations() {
        return sections.getStations();
    }
}
