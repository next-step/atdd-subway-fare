package nextstep.subway.domain;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalTimeConverter;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private Integer additionalFare;
    private LocalTime firstTime;
    private LocalTime lastTime;
    private int intervalMinute;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {
    }

    public Line(String name, String color) {
        this(name, color, 0);
    }

    public Line(String name, String color, Integer additionalFare) {
        this(name, color, additionalFare, LocalTime.of(0, 0), LocalTime.of(0, 0), 0);
    }

    public Line(String name, String color, Integer additionalFare, LocalTime firstTime, LocalTime lastTime, int intervalMinute) {
        if (firstTime.isAfter(lastTime)) {
            throw new IllegalArgumentException("첫차 시간이 막차 시간보다 늦을 수 없습니다.");
        }
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        this.firstTime = firstTime;
        this.lastTime = lastTime;
        this.intervalMinute = intervalMinute;
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

    public Integer getAdditionalFare() {
        return additionalFare;
    }

    public LocalTime getFirstTime() {
        return firstTime;
    }

    public LocalTime getLastTime() {
        return lastTime;
    }

    public int getIntervalMinute() {
        return intervalMinute;
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public void update(String name, String color, Integer additionalFare) {
        if (name != null) {
            this.name = name;
        }
        if (color != null) {
            this.color = color;
        }
        if (additionalFare != null) {
            this.additionalFare = additionalFare;
        }
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        addSection(upStation, downStation, distance, 0);
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

    public List<LocalTime> getSchedule() {
        return null;
    }
}
