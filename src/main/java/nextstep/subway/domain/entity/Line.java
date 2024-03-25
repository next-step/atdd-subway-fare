package nextstep.subway.domain.entity;

import javax.persistence.*;

@Entity
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String color;

	@Embedded
	private Sections sections;

	private int additionalFee;

	protected Line() {

	}

	public Line(String name, String color, Long startStationId, Long endStationId, int distance, int duration) {
		this.name = name;
		this.color = color;
		this.sections = new Sections(startStationId, endStationId, distance, duration);
		this.additionalFee = 0;
		sections.addSection(new Section(this, startStationId, endStationId, distance, duration));
	}

	public Line(String name, String color, Long startStationId, Long endStationId, int distance, int duration, int additionalFee) {
		this.name = name;
		this.color = color;
		this.sections = new Sections(startStationId, endStationId, distance, duration);
		this.additionalFee = additionalFee;
		sections.addSection(new Section(this, startStationId, endStationId, distance, duration));
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public Long getStartStationId() {
		return sections.getStartStationId();
	}

	public Long getEndStationId() {
		return sections.getEndStationId();
	}

	public int getDistance() {
		return sections.getDistance();
	}

	public int getDuration() {
		return sections.getDuration();
	}

	public Sections getSections() {
		return sections;
	}

	public int getAdditionalFee() {
		return additionalFee;
	}

	public void setUpdateInfo(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public void addSection(Section section) {
		sections.addSection(this, section);
	}

	public void deleteSection(Long stationId) {
		sections.deleteSection(this, stationId);
	}

	public boolean hasStation(Long stationId) {
		return sections.hasStation(stationId);
	}
}
