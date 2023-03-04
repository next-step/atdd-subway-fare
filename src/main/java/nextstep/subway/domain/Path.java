package nextstep.subway.domain;


import nextstep.subway.domain.Fare.Fare;

import java.util.List;

public class Path {
	private Sections sections;

	public Path(Sections sections) {
		this.sections = sections;
	}

	public Sections getSections() {
		return sections;
	}

	public int extractDistance() {
		return sections.totalDistance();
	}
	public int extractFare(int age) {
		return Fare.calculate(extractDistance(), sections.extraFare(), age);
	}
	public List<Station> getStations() {
		return sections.getStations();
	}

	public int extractDuration() {
		return sections.totalDuration();
	}
}
