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

	public int extractCharge() {
		int totalDistance = extractDistance();

		if (totalDistance <= 10) {
			return 1250;
		}
		if (totalDistance <= 15) {
			return 1350;
		}
		if (totalDistance <= 20) {
			return 1450;
		}
		if (totalDistance <= 50) {
			return 2050;
		}
		if (totalDistance <= 106) {
			return 2750;
		}
		return 0;
	}

    public List<Station> getStations() {
        return sections.getStations();
    }
}
