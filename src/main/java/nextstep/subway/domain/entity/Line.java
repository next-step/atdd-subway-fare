package nextstep.subway.domain.entity;

import nextstep.subway.domain.entity.addition.SectionAdditionHandlerMapping;
import nextstep.subway.domain.entity.deletion.SectionDeletionHandlerMapping;
import nextstep.subway.domain.vo.Sections;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;
    
    @Column(nullable = false)
    private Integer distance;

    @Embedded
    private Sections sections;

    protected Line() {

    }

    public Line(String name, String color, Integer distance, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.sections = Sections.init(new Section(this, upStation, downStation, distance));
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public List<Section> getSectionList() {
        return sections.getSections();
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

    public Integer getDistance() {
        return distance;
    }

    public Sections getSections() {
        return sections;
    }

    public void addSection(SectionAdditionHandlerMapping handlerMapping, Section section) {
        sections.addSection(handlerMapping, section);
    }

    public void removeSection(SectionDeletionHandlerMapping handlerMapping, Station station) {
        sections.remove(handlerMapping, station);
    }

    public boolean hasStation(Station station) {
        return sections.hasStation(station);
    }
}
