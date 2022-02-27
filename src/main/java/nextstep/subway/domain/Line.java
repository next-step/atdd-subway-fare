package nextstep.subway.domain;

import java.util.Objects;
import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private Integer additionalFare;
    private String startTime;
    private String endTime;
    private int intervalTime;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    private Line(String name, String color, int additionalFare) {
        this(name, color, additionalFare, "", "", 0);
    }

    private Line(String name, String color, int additionalFare, String startTime, String endTime, int intervalTime) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
    }

    public static Line of(String name, String color, int additionalFare, String startTime, String endTime, int intervalTime) {
        return new Line(name, color, additionalFare, startTime, endTime, intervalTime);
    }

    public static Line of(String name, String color, int additionalFare) {
        return new Line(name, color, additionalFare);
    }

    public static Line of(String name, String color) {
        return new Line(name, color, 0);
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

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return intervalTime == line.intervalTime && Objects.equals(id, line.id)
            && Objects.equals(name, line.name) && Objects.equals(color, line.color)
            && Objects.equals(additionalFare, line.additionalFare)
            && Objects.equals(startTime, line.startTime) && Objects.equals(endTime,
            line.endTime) && Objects.equals(sections, line.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, additionalFare, startTime, endTime, intervalTime,
            sections);
    }
}
