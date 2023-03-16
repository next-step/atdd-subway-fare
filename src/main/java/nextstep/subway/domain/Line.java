package nextstep.subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private int extraFare;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color, int extraFare) {
        validateMinusFare(extraFare);
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    private void validateMinusFare(int extraFare) {
        if (extraFare < 0) {
            throw new IllegalArgumentException("노선 추가 요금을 음수로 설정할 수 없습니다.");
        }
    }

    public Line(String name, String color) {
        this(name, color, 0);
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

    public int getExtraFare() {
        return extraFare;
    }

    public List<Section> getSections() {
        return sections.getSections();
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

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void deleteSection(Station station) {
        sections.delete(station);
    }

    public List<Section> getOppositeSections() {
        return sections.getOppositeSections();
    }
}
