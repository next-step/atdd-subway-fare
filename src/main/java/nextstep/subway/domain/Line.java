package nextstep.subway.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private int surcharge;

    private LocalTime startTime;

    private LocalTime endTime;

    private int intervalTime;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this(name, color, 0, null, null, 0);
    }

    public Line(String name, String color, int surcharge) {
        this(name, color, surcharge, null, null, 0);
    }

    public Line(String name, String color, int surcharge, LocalTime startTime, LocalTime endTime, int intervalTime) {
        if (surcharge < 0) {
            throw new IllegalArgumentException("지하철 노선의 추가요금은 0원 이상이어야 합니다.");
        }

        this.name = name;
        this.color = color;
        this.surcharge = surcharge;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
    }

    public LocalDateTime getStopTime(LocalDateTime current, Station station, boolean isGoingUp) {
        return sections.getStopTime(current, startTime, endTime, intervalTime, station, isGoingUp);
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

    public int getSurcharge() {
        return surcharge;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void update(String name, String color, int surcharge) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
        this.surcharge = surcharge;
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
}
