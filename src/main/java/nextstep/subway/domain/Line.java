package nextstep.subway.domain;

import javax.persistence.*;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String color;

	private int addLineFare;

	@Embedded
	private Sections sections = new Sections();

	public Line(String name, String color) {
		this.name = name;
		this.color = color;
		this.addLineFare = 0;
	}

	public Line(String name, String color, int addLineFare) {
		this.name = name;
		this.color = color;
		this.addLineFare = addLineFare;
	}

	public List<Section> getSections() {
		return sections.getSections();
	}

	public List<Station> getStations() {
		return sections.getStations();
	}

	public void update(String name, String color) {
		if (name != null) {
			this.name = name;
		}
		if (color != null) {
			this.color = color;
		}
	}

	public void addSection(Station upStation, Station downStation, int distance, int duration) {
		sections.add(new Section(this, upStation, downStation, distance, duration));
	}

	public void deleteSection(Station station) {
		sections.delete(station);
	}
}
