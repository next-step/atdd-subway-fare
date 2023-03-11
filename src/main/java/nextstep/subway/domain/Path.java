package nextstep.subway.domain;

import java.util.List;

public class Path {
	private Sections sections;
	private Fare fare;

	public Path(Sections sections) {
		this.sections = sections;
		this.fare = Fare.of(extractDistance());
	}

	public Sections getSections() {
		return sections;
	}

	public int extractDistance() {
		return sections.totalDistance();
	}

	public List<Station> getStations() {
		return sections.getStations();
	}

	public int extractDuration() {
		return sections.totalDuration();
	}

	public int calculateFare() {
		return fare.getFare();
	}
}
