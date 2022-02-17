package nextstep.subway.domain;

import javax.persistence.*;

import java.time.LocalTime;
import java.util.List;

import jdk.vm.ci.meta.Local;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "LINE_NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "LINE_COLOR", nullable = false)
    private String color;

    @Column(name = "LINE_ADDITIONAL_FARE", nullable = false)
    private int additionalFare;

    @Column(name = "LINE_START_TIME", nullable = false)
    private LocalTime startTime;

    @Column(name = "LINE_END_TIME", nullable = false)
    private LocalTime endTime;

    @Column(name = "LINE_INTERVAL_TIME", nullable = false)
    private LocalTime intervalTime;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color, int additionalFare, LocalTime startTime, LocalTime endTime, LocalTime intervalTime) {
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalTime getIntervalTime() {
        return intervalTime;
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

    public SubwayDispatchTime getDispatchTime() {
        return new SubwayDispatchTime(startTime, endTime, intervalTime);
    }
}
