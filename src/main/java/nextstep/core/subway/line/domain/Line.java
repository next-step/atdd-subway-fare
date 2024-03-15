package nextstep.core.subway.line.domain;

import nextstep.core.subway.section.domain.Section;
import nextstep.core.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {

    public static final int MIN_ADDITIONAL_FARE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private int additionalFare;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color, int additionalFare) {
        this.name = name;
        this.color = color;
        this.additionalFare = validateAdditionalFare(additionalFare);
    }

    private int validateAdditionalFare(Integer additionalFare) {
        if (additionalFare == null || additionalFare < MIN_ADDITIONAL_FARE) {
            throw new IllegalArgumentException("추가 요금은 0 원 이상이어야 합니다.");
        }
        return additionalFare;
    }

    public Line update(String name, String color) {
        this.name = name;
        this.color = color;
        return this;
    }


    public void addSection(Section sectionToAdd) {
        if (canAddSection(sectionToAdd)) {
            sections.addSection(sectionToAdd);
        }
    }

    public void delete(Station stationToDelete) {
        if (sections.canDeleteSection(stationToDelete)) {
            sections.deleteSection(stationToDelete);
        }
    }

    private boolean canAddSection(Section sectionToAdd) {
        if (sectionToAdd.areStationsSame()) {
            throw new IllegalArgumentException("추가할 구간의 상행역과 하행역은 동일할 수 없습니다.");
        }
        if (sections.hasNoSections()) {
            return true;
        }
        if (hasStations(sectionToAdd)) {
            throw new IllegalArgumentException("이미 노선에 포함되어 있는 상행역과 하행역입니다.");
        }
        if (!hasExactlyOneStation(sectionToAdd)) {
            throw new IllegalArgumentException("노선에 연결할 수 있는 상행역 혹은 하행역이 아닙니다.");
        }
        return true;
    }

    private boolean hasExactlyOneStation(Section station) {
        return hasExistingStation(station.getUpStation()) ^
                hasExistingStation(station.getDownStation());
    }

    private boolean hasStations(Section station) {
        return hasExistingStation(station.getUpStation()) &&
                hasExistingStation(station.getDownStation());
    }

    private boolean hasExistingStation(Station station) {
        return sections.hasExistingStation(station);
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

    public int getAdditionalFare() {
        return additionalFare;
    }

    public List<Station> getAllStations() {
        return sections.getAllStations();
    }

    public List<Section> getSortedAllSections() {
        return sections.getSortedAllSections();
    }
}
