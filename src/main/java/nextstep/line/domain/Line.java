package nextstep.line.domain;


import nextstep.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "line")
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private Color color;

    @Embedded
    private LineSections lineSections = new LineSections();

    @Column(nullable = false)
    private Integer distance;
    @Column(nullable = false)
    private Integer duration;

    private Integer extraFare;

    public Line() {
    }

    public Line(String name, Color color, Station upStation, Station downStation, int distance, int duration, int extraFare) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.duration = duration;
        this.extraFare = extraFare;
        lineSections.add(new Section(upStation, downStation, distance, duration, this));
    }


    public Line(String name, Color color, Station upStation, Station downStation, int distance, int duration) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.duration = duration;
        lineSections.add(new Section(upStation, downStation, distance, duration, this));
    }

    public void update(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        lineSections.add(section);
        this.distance = lineSections.getSumDistance();
        this.duration = lineSections.getSumDuration();
    }


    public List<Station> getStations() {
        return lineSections.getStations();
    }


    public boolean deletableSection() {
        return lineSections.deletable();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }


    public Integer getDistance() {
        return distance;
    }

    public Station getFirstStation() {
        return lineSections.getFirstStation();
    }

    public Station getLastStation() {
        return lineSections.getLastStation();
    }

    public void removeStation(Station station) {
        if (!this.deletableSection()) {
            throw new IllegalStateException("구간이 1개여서 역을 삭제할 수 없다");
        }
        lineSections.remove(station);
    }

    public List<Section> getLineSections() {
        return lineSections.getSections();
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getExtraFare() {
        return extraFare;
    }
}
